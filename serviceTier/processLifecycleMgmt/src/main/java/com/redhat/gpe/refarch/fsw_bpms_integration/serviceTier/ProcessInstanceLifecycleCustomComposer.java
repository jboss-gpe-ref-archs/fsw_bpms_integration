package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;


import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.component.resteasy.composer.RESTEasyBindingData;
import org.switchyard.component.resteasy.composer.RESTEasyMessageComposer;
import org.apache.log4j.Logger;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;
import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

public class ProcessInstanceLifecycleCustomComposer extends RESTEasyMessageComposer {
	
    private static final String INTEGER_FLAG = "i";
    private static Logger log = Logger.getLogger("ProcessInstanceLifecycleCustomComposer");

    @Override
    public Message compose(RESTEasyBindingData source, Exchange exchange) throws Exception {
        final Message message = super.compose(source, exchange);
        log.debug("compose() source = "+source);
        return message;
    }

    @Override
    public RESTEasyBindingData decompose(Exchange exchange, RESTEasyBindingData target) throws Exception {
        ProcessDetails pDetails = (ProcessDetails)exchange.getMessage().getContent();
        String opName = exchange.getContract().getProviderOperation().getName();
        log.debug("decompose() opName = "+opName+" : pDetails = "+pDetails);
        
        target = super.decompose(exchange, target);
       
        if(ProcessInstanceLifecycleResource.START_PROCESS_REST.equals(opName)) {
        	Policy policyObj = getPolicy(pDetails);
            target.setParameters(new Object[]{pDetails.getDeploymentId(), pDetails.getProcessId(), policyObj.getPolicyId()+INTEGER_FLAG, policyObj.getPolicyName()});
        }else if(ProcessInstanceLifecycleResource.START_PROCESS_EXECUTOR.equals(opName)) {
            target.setParameters(new Object[]{pDetails.getDeploymentId(), getCommandRequestPayload(pDetails)});
        }else {
            throw new RuntimeException("decompose() providerOperationName is invalid: "+opName);
        }
        return target;
    }
    
    private Policy getPolicy(ProcessDetails pDetails) throws Exception {
    	String policyString = pDetails.getPayload();
    	JAXBContext jaxbContext = JAXBContext.newInstance(Policy.class);
        Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
        StringReader sReader = new StringReader(policyString);
        Policy policyObj = (Policy)jaxbUnMarshaller.unmarshal(sReader);
    	return policyObj;
    }
    
    private String getCommandRequestPayload(ProcessDetails pDetails) throws Exception {
    	return null;
    }

}
