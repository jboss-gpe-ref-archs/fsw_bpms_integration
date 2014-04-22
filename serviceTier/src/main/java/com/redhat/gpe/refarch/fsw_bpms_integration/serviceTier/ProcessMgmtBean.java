package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Random;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.switchyard.Context;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;
import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

/**
 * TO-DO:
 *    1)  send policy object as payload of startProcess POST
 *    2)  specify processId to start as per the message payload to this component
 *    3)  specify role used to query for potential tasks
 *    4)  identify tasks that have already been claimed and iterate to next task in NodeList
 *    5)  demonstrate invocation of the following BPM Suite 6 task operation:  claimnextavailable 
 *
 */
@Service(ProcessMgmt.class)
public class ProcessMgmtBean implements ProcessMgmt {

    private static final String BPMS_EXEC_SERVER_HOSTNAME = "bpms.exec.server.hostname";
    private static final String BPMS_EXEC_SERVER_PORT = "bpms.exec.server.port";
    private static final String BPMS_EXEC_SERVER_USER_ID = "bpms.exec.server.userId";
    private static final String BPMS_EXEC_SERVER_PASSWORD = "bpms.exec.server.passwd";
    private static final String START_PROCESS_ID_PATH = "/process-instance/id";
    private static final String TASK_ID_LIST_PATH = "/task-summary-list/task-summary/id";
    private static final String ID = "id";
    private static Logger log = Logger.getLogger("ProcessMgmtBean");
    private static Random random = new Random();
    
    @Inject
    private Context context;
    
    @Inject @Reference("PInstanceLifecycle")
    private ProcessInstanceLifecycle pInstanceLifecycle;
    
    @Inject @Reference("TaskLifecycle")
    private TaskLifecycle taskLifecycle;

    public ProcessMgmtBean() {
        StringBuilder sBuilder = new StringBuilder("ProcessMgmtBean() props = \n\t");
        sBuilder.append("bpms.exec.server.hostname= ");
        sBuilder.append(System.getProperty(BPMS_EXEC_SERVER_HOSTNAME));
        sBuilder.append("\n\tbpms.exec.server.port=");
        sBuilder.append(System.getProperty(BPMS_EXEC_SERVER_PORT));
        sBuilder.append("\n\tbpms.exec.server.userId=");
        sBuilder.append(System.getProperty(BPMS_EXEC_SERVER_USER_ID));
        sBuilder.append("\n\tbpms.exec.server.passwd=");
        sBuilder.append(System.getProperty(BPMS_EXEC_SERVER_PASSWORD));
        log.info(sBuilder.toString());
    }

    public void executeProcessLifecycle(ProcessDetails pDetails) {
        log.info("executeProcessLifecycle() pDetails = "+pDetails);
      
        try {
            // 1)  Start Process
            String policyJaxb = getPolicyJaxb(); 
            String processId = pDetails.getProcessId();
            String httpEntity = pInstanceLifecycle.startProcess(processId);
            String pInstanceId = getNodeFromXPath(httpEntity, START_PROCESS_ID_PATH, -1).getTextContent();
            log.info("executeProcessLifecycle() pInstanceId = "+pInstanceId);
            
            // 2)  Query for tasks
            httpEntity =  taskLifecycle.queryForPotentialTasks("reviewer");
            
            NodeList taskIds = getNodeListFromXPath(httpEntity, TASK_ID_LIST_PATH);
            log.info("executeProcessLifecycle() # of tasks returned = "+taskIds.getLength());
            String taskId = null;
            for(int y=0; y < taskIds.getLength(); y++){
                Node taskIdNode = taskIds.item(y);
                taskId = taskIdNode.getTextContent();
                
                // 3)  Attempt to claim the task and break out of loop if successful
                log.info("executeProcessLifecycle() about to claim task with id = "+taskId);
                Response response = taskLifecycle.claimTask(taskId);
                if(response.getStatus() == 200) {
                    log.info("executeProcessLifecycle() just claimed task with Id = "+taskId);
                    break;
                }else {
                    log.info("executeProcessLifecycle() attempt to claim task with id = "+taskId+" returned response status of: "+response.getStatus()+".  Will try next potential task");
                }
                
            }
            log.info("executeProcessLifecycle() about to start task with taskId = "+taskId);
            taskLifecycle.startTask(taskId);
            
            log.info("executeProcessLifecycle() about to complete task with taskId = "+taskId);
            taskLifecycle.completeTask(taskId);
        }catch(Throwable x) {
            log.error("executeProcessLifecycle() 000001:  Throwable = "+x.getLocalizedMessage());
            x.printStackTrace();
        }
    }

    // Policy Jaxb representation sent to processTier and used as process instance variable
    private String getPolicyJaxb() throws JAXBException {
        Policy policyObj = new Policy(random.nextInt(1000));
        JAXBContext jaxbContext = JAXBContext.newInstance(Policy.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(policyObj, sw);
        return sw.toString();
    }
    
    private Node getNodeFromXPath(String httpResponseEntity, String xPath, int nodeListNumber) throws Exception {
        log.info("getNodeFromXPath() xPath = "+xPath+" : entity = "+httpResponseEntity);
        
        // wouldn't be a bad idea to pool these compiled xpathExpressions along with DocumentBuilderFactory
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xpathExpression = xpath.compile(xPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document httpEntityDoc = dBuilder.parse(new ByteArrayInputStream(httpResponseEntity.getBytes()));
        
        Node nodeObj = null;
        if(nodeListNumber == -1 )
            nodeObj = (Node)xpathExpression.evaluate(httpEntityDoc.getDocumentElement(), XPathConstants.NODE);
        else{
            NodeList nList = (NodeList)xpathExpression.evaluate(httpEntityDoc, XPathConstants.NODESET);
            nodeObj = nList.item(nodeListNumber);
        }
        return nodeObj;
    }
    
    private NodeList getNodeListFromXPath(String httpResponseEntity, String xPath) throws Exception {
        log.info("getNodeListFromXPath() xPath = "+xPath+" : entity = "+httpResponseEntity);
        
        // wouldn't be a bad idea to pool these compiled xpathExpressions along with DocumentBuilderFactory
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xpathExpression = xpath.compile(xPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document httpEntityDoc = dBuilder.parse(new ByteArrayInputStream(httpResponseEntity.getBytes()));
        
        return (NodeList)xpathExpression.evaluate(httpEntityDoc, XPathConstants.NODESET);
    }
}
