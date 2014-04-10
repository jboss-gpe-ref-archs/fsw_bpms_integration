package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.util.Random;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * JMS client to send a message to a queue
 *
 * sample command-line execution:
 *   mvn clean test exec:java -Dexec.mainClass="com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier.JMSClient" -Dexec.classpathScope="test" -DHOST_URL=localhost -DPORT=5445 -DUSER=fswAdmin -DPASSWORD=jb0ssredhat!
 *
 */
public final class JMSClient {
    
    private static final String QUEUE_NAME = "QUEUE_NAME";
    
    private static String dName = "PolicyEvaluation";         // queue name to send message it
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private static Random rGenerator = new Random();
    private static IJMSClientProvider jmsProvider = new HornetQClientProvider();
    
    private JMSClient() {}
    
    public static void main(final String[] ignored) throws Exception {
    	
    	dName = System.getProperty(QUEUE_NAME, dName);
    	Connection connection = jmsProvider.getConnection();
        
        Session session = null;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue dQueue = jmsProvider.getQueue(dName);
            final MessageProducer producer = session.createProducer(dQueue);
                String jsonRep = "test";
                Message message = session.createTextMessage(jsonRep);
                producer.send(message);
             
        } finally {
            if(connection != null)
            	connection.close();
        }
    }
}
