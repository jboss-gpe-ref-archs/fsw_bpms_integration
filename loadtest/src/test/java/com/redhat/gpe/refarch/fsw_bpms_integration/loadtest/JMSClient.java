package com.redhat.gpe.refarch.fsw_bpms_integration.loadtest;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Logger;

/**
 * JMS client to send a message to a queue
 */
public final class JMSClient extends AbstractJavaSamplerClient {

    private static final String PATH_TO_LOG4J_CONFIG = "path.to.log4j.xml";
    private static final String QUEUE_NAME = "queue.name";
    
    private static String dName = "PolicyEvaluation";         // queue name to send message it
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private static Random rGenerator = new Random();
    private static IJMSClientProvider jmsProvider = new HornetQClientProvider();
    private static Logger log = Logger.getLogger("JMSClient");
    private Connection connection = null;

    static{
        String pathToLog4jConfig = System.getProperty(PATH_TO_LOG4J_CONFIG);
        if(pathToLog4jConfig != null && !pathToLog4jConfig.equals("")) {
            DOMConfigurator.configure(pathToLog4jConfig);
        }
    }
    
    public JMSClient() {}
    
    public SampleResult runTest(JavaSamplerContext context){
        SampleResult results = new SampleResult();
        results.sampleStart();
        try {
            prep();
            send();
            results.setResponseMessage("success");
            results.setSuccessful(true);
            results.setResponseCodeOK();
        }catch(Throwable x){
            StringWriter sw = new StringWriter();
            x.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            log.error("runTest() stackTrace = "+stackTrace);
            results.setResponseMessage(stackTrace);
            results.setSuccessful(false);
        }
        return results;
    }
    
    private void prep() throws JMSException {
        dName = System.getProperty(QUEUE_NAME, dName);
        connection = jmsProvider.getConnection();
    }
    
    private void send() throws JMSException {
        Session session = null;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue dQueue = jmsProvider.getQueue(dName);
            final MessageProducer producer = session.createProducer(dQueue);
            String jsonRep = "test";
            Message message = session.createTextMessage(jsonRep);
            producer.send(message);
            log.info("send() just sent message = "+message);
            
        } finally {
            if(connection != null)
                connection.close();
        }
        
    }
    
    /* sample command-line execution:
    *   mvn clean test exec:java -Dexec.mainClass="com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier.JMSClient" -Dexec.classpathScope="test" -DHOST_URL=localhost -DPORT=5445 -DUSER=fswAdmin -DPASSWORD=jb0ssredhat!
    */
    public static void main(final String[] ignored) throws Exception {
        JMSClient jmsClient = new JMSClient();
        jmsClient.prep();
        jmsClient.send();
    }
}
