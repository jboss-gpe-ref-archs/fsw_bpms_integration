package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.hornetq.jms.client.HornetQQueue;

public class HornetQClientProvider implements IJMSClientProvider {
	
	private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";
    private static final String HOST_URL = "HOST_URL";
    private static final String PORT = "PORT";
    
    private static String userId = "guest";                   // as per EAP messaging sub-system
    private static String passwd = "guestp.1";                // as per EAP messaging sub-system
    private static String hostUrl = "localhost";              // ipAddress/dnsName that messaging broker is bound to
    private static int port = 5445;                           // tcp port that messaging broker is listening on
    private static HornetQConnectionFactory cFactory = null;
    private static Object lockObj = new Object();
    
    static{
    	userId = System.getProperty(USER, userId);
    	passwd = System.getProperty(PASSWORD, passwd);
    	hostUrl = System.getProperty(HOST_URL,hostUrl);
    	port = Integer.parseInt(System.getProperty(PORT, Integer.toString(port)));
    	
    	StringBuffer sBuffer = new StringBuffer("static{} sending message using following properties : ");
    	sBuffer.append("userId = ");
    	sBuffer.append(userId);
    	sBuffer.append(" : passwd=");
    	sBuffer.append(passwd);
    	sBuffer.append(" : hostUrl=");
    	sBuffer.append(hostUrl);
    	sBuffer.append(" : port=");
    	sBuffer.append(port);
    	System.out.println(sBuffer.toString());
    }
    public HornetQClientProvider(){}

    public Connection getConnection() throws JMSException {
    	if(cFactory == null){
    		synchronized(lockObj){
    			if(cFactory != null)
    				return cFactory.createConnection();

    			Map<String, Object> params = new HashMap<String,Object>();
    			params.put(TransportConstants.HOST_PROP_NAME, hostUrl);
    			params.put(TransportConstants.PORT_PROP_NAME, port);
    			cFactory = new HornetQConnectionFactory(false, new TransportConfiguration(NettyConnectorFactory.class.getName(), params));
    		}
    	}
    	return cFactory.createConnection();
    }

	public Queue getQueue(String name) throws JMSException {
		return new HornetQQueue(name);
	}

}
