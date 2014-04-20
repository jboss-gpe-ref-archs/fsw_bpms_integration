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
import org.apache.log4j.Logger;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;
import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

@Service(ProcessMgmt.class)
public class ProcessMgmtBean implements ProcessMgmt {

	private static final String BPMS_EXEC_SERVER_HOSTNAME = "bpms.exec.server.hostname";
	private static final String BPMS_EXEC_SERVER_PORT = "bpms.exec.server.port";
	private static final String START_PROCESS_ID_PATH = "/process-instance/id";
    private static Logger log = Logger.getLogger("ProcessMgmtBean");
    private static Random random = new Random();
    
    
    @Inject
    private Context context;
    
    @Inject @Reference("StartProcess")
    private StartProcess sProcessAction;
    
    @Inject @Reference("GetPotentialTasks")
    private GetPotentialTasks gPotentialTasks;

    public ProcessMgmtBean() {
    	log.info("ProcessMgmtBean() ****** ");
    }

    public void executeProcessLifecycle(ProcessDetails pDetails) {
    	StringBuilder sBuilder = new StringBuilder("executeProcessLifecycle() props = \n\t");
    	sBuilder.append("bpms.exec.server.hostname= ");
    	sBuilder.append(context.getPropertyValue(BPMS_EXEC_SERVER_HOSTNAME));
    	sBuilder.append("\n\tbpms.exec.server.port=");
    	sBuilder.append(context.getPropertyValue(BPMS_EXEC_SERVER_PORT));
    	log.info(sBuilder.toString());
        log.info("executeProcessLifecycle() pDetails = "+pDetails);
      
        try { 
            String policyJaxb = getPolicyJaxb(); 
            String httpEntity = sProcessAction.start(policyJaxb);
            String processId = getProcessId(httpEntity);
            

            //gPotentialTasks.getPotentialTasks();
        }catch(Throwable x) {
            x.printStackTrace();
        }
    }

    private String getPolicyJaxb() throws JAXBException {
        Policy policyObj = new Policy(random.nextInt(1000));
        JAXBContext jaxbContext = JAXBContext.newInstance(Policy.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(policyObj, sw);
        return sw.toString();
    }
    
    private String getProcessId(String httpEntity) throws Exception {
    	/*
    	 * <process-instance>
    	 *   <status>SUCCESS</status>
    	 *   <url>http://bpmsjb-bpmstraining.apps.lab2.opentlc.com/business-central/rest/runtime/com.redhat.gpe.refarch.fsw_bpms_integration:processTier:1.0/process/processTier.policyQuoteProcess/start</url>
    	 *   <process-id>processTier.policyQuoteProcess</process-id>
    	 *   <id>3</id>
    	 *   <state>1</state>
    	 * </process-instance>
    	 */
    	log.info("getProcessId() entity = "+httpEntity);
    	
    	// wouldn't be a bad idea to pool these compiled xpathExpressions along with DocumentBuilderFactory
    	XPath xpath = XPathFactory.newInstance().newXPath();
    	XPathExpression startProcessXPath = xpath.compile(START_PROCESS_ID_PATH);
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = factory.newDocumentBuilder();
    	Document httpEntityDoc = dBuilder.parse(new ByteArrayInputStream(httpEntity.getBytes()));
    	
    	Node pInstanceId = (Node)startProcessXPath.evaluate(httpEntityDoc.getDocumentElement(), XPathConstants.NODE);
    	log.info("getProcessId() processId = "+pInstanceId.getTextContent());
    	return pInstanceId.getTextContent();
    }
}
