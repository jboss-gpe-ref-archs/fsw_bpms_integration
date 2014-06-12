package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.switchyard.component.test.mixins.http.HTTPMixIn;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *  Example invocation:   
 *    mvn clean test exec:java -Dbpms.exec.server.hostname=bpmsjb-bpmstraining.apps.lab2.opentlc.com -Dbpms.exec.server.port=80
 */
public class ProcessMgmtTest {

    private static final String UTF_8 = "UTF-8";
    private static final String BPMS_EXEC_SERVER_HOSTNAME = "bpms.exec.server.hostname";
    private static final String BPMS_EXEC_SERVER_PORT = "bpms.exec.server.port";
    private static final String RUNTIME_CONTEXT = "runtime/";
    private static final String TASK_CONTEXT = "task/";
    private static final String DEPLOYMENT_ID = "com.redhat.gpe.refarch.fsw_bpms_integration:processTier:1.0";
    private static final String PROCESS_ID = "processTier.policyQuoteProcess";
    private static final String TEST_START_PROCESS_RESPONSE = "<process-instance><status>SUCCESS</status><url>http://bpmsjb-bpmstraining.apps.lab2.opentlc.com/business-central/rest/runtime/com.redhat.gpe.refarch.fsw_bpms_integration:processTier:1.0/process/processTier.policyQuoteProcess/start</url><process-id>processTier.policyQuoteProcess</process-id><id>13</id><state>1</state></process-instance>";

    public static void main(String[] args) throws Exception {
        //testStartProcess();
        testDocAndXPath();
    }

    private static void testStartProcess() throws Exception {
        String host = System.getProperty(BPMS_EXEC_SERVER_HOSTNAME, "localhost");
        int port = Integer.parseInt(System.getProperty(BPMS_EXEC_SERVER_PORT, "8080"));
        String baseUrl="http://"+host+":"+port+"/business-central/rest/";
        System.out.println("main() baseUrl = "+baseUrl);
        HTTPMixIn http = new HTTPMixIn();
        http.initialize();
        http.setContentType("application/x-www-form-urlencoded");
        int status = http.sendStringAndGetStatus(baseUrl + RUNTIME_CONTEXT+DEPLOYMENT_ID+"/process/"+PROCESS_ID+"/start", "", HTTPMixIn.HTTP_POST);
        Assert.assertEquals(401, status);
        http.setRequestHeader("Authorization", "Basic amJvc3M6YnJtcw=");
        HttpResponse response = http.sendStringAndGetMethod(baseUrl + RUNTIME_CONTEXT+DEPLOYMENT_ID+"/process/"+PROCESS_ID+"/start", "", HTTPMixIn.HTTP_POST);
        System.out.println("main() startProcess statusLine = "+response.getStatusLine());
        String responseString = EntityUtils.toString(response.getEntity(), UTF_8);
        //System.out.println("main() startProcess response = "+responseString);
    }

    private static void testDocAndXPath() throws Exception {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document httpEntityDoc = dBuilder.parse((new InputSource(new StringReader(TEST_START_PROCESS_RESPONSE))));
        String nodeValue = httpEntityDoc.getElementsByTagName("id").item(0).getTextContent();
        System.out.println("firstNode = "+nodeValue);
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression startProcessXPath = xpath.compile("/process-instance/id");
        Node pInstanceId = (Node)startProcessXPath.evaluate(httpEntityDoc.getDocumentElement(), XPathConstants.NODE);
        
        System.out.println("testXPath() pInstanceId = "+pInstanceId.getTextContent());
    }
}
