package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;

public interface IJMSClientProvider {
	
	Connection getConnection() throws JMSException;
	Queue getQueue(String name) throws JMSException;
	
}
