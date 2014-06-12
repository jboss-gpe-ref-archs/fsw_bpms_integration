package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.io.ByteArrayInputStream;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.switchyard.Context;
import org.switchyard.Property;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

/**
 *
 */
@Service(ProcessMgmt.class)
public class ProcessMgmtBean implements ProcessMgmt {

    private static final String BPMS_EXEC_SERVER_HOSTNAME = "bpms.exec.server.hostname";
    private static final String BPMS_EXEC_SERVER_PORT = "bpms.exec.server.port";
    private static final String BPMS_EXEC_SERVER_USER_ID = "bpms.exec.server.userId";
    private static final String BPMS_EXEC_SERVER_PASSWORD = "bpms.exec.server.passwd";
    private static final String REQUEST_TIMEOUT_MILLIS = "request.timeout.millis";
    private static final String START_PROCESS_ID_PATH = "/process-instance/id";
    private static final String TASK_ID_LIST_PATH = "/task-summary-list/task-summary/id";
    private static final String ID = "id";
    private static Logger log = Logger.getLogger("ProcessMgmtBean");
    
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
        sBuilder.append("\n\trequest.timeout.millis=");
        sBuilder.append(System.getProperty(REQUEST_TIMEOUT_MILLIS));
        log.info(sBuilder.toString());
    }

    public void executeProcessLifecycleViaRest(ProcessDetails pDetails) {
        
        log.info("executeProcessLifecycle() pDetails = "+pDetails);
      
        try {
            // 1)  Start Process and parse for processId from response
            String httpEntity = pInstanceLifecycle.startProcessRest(pDetails);
            String pInstanceId = getNodeFromXPath(httpEntity, START_PROCESS_ID_PATH, -1).getTextContent();
            log.info("executeProcessLifecycle() pInstanceId = "+pInstanceId);
            
            //1.5)  break out of this function now if been instructed to NOT execute task lifecycle on this process
            if(!pDetails.getExecuteTaskLifecycle())
                return;
            
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
            
            // 4)  start the previously claimed task
            log.info("executeProcessLifecycle() about to start task with taskId = "+taskId);
            taskLifecycle.startTask(taskId);
            
            // 5)  complete the previously started task
            log.info("executeProcessLifecycle() about to complete task with taskId = "+taskId);
            pDetails.setTaskId(taskId);
            taskLifecycle.completeTask(pDetails);
        }catch(Throwable x) {
            log.error("executeProcessLifecycle() 000001:  Throwable = "+x.getLocalizedMessage());
            x.printStackTrace();
        }
    }

    public void executeProcessLifecycleViaExecutionAPI(ProcessDetails pDetails) {
        log.info("executeProcessLifecycleViaExecutionAPI() pDetails = "+pDetails);
        try {
        }catch(Throwable x) {
            log.error("executeProcessLifecycleViaExecutionAPI() 000002:  Throwable = "+x.getLocalizedMessage());
            x.printStackTrace();
        }
    }
    
    private Node getNodeFromXPath(String httpResponseEntity, String xPath, int nodeListNumber) throws Exception {
        log.debug("getNodeFromXPath() xPath = "+xPath+" : entity = "+httpResponseEntity);
        
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
        log.debug("getNodeListFromXPath() xPath = "+xPath+" : entity = "+httpResponseEntity);
        
        // wouldn't be a bad idea to pool these compiled xpathExpressions along with DocumentBuilderFactory
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xpathExpression = xpath.compile(xPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document httpEntityDoc = dBuilder.parse(new ByteArrayInputStream(httpResponseEntity.getBytes()));
        
        return (NodeList)xpathExpression.evaluate(httpEntityDoc, XPathConstants.NODESET);
    }
    
    private void logContextVariables() {
        Set<Property> propsSet = context.getProperties();
        StringBuilder sBuilder = new StringBuilder();
        for(Property prop : propsSet){
            sBuilder.append("\n\tpropName="+prop.getName()+"\tvalue="+prop.getValue());
        }
        log.info("logContextVariables() context props = "+sBuilder.toString());
    }
}
