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

/*
    As of FSW 6.0.0, only one pojo can be passed to a composite reference binding.
    The composite reference binding in this reference architecture is implemented using RESTeasy Client Proxy.
    As per the TaskLifecycleResource.java JAX-RS client interface, the functions defined in that interface take multiple parameters.
    The purpose of this class is to map between :
        1)  inbound pojo on the SwitchYard/Camel exchange
        2)  the parameters required to invoke the remote BPM Suite 6 Execution Server resources via the JAX-RS client.
 */
public class TaskLifecycleCustomComposer extends RESTEasyMessageComposer {
    
    private static final String INTEGER_FLAG = "i";
    private static final String MY_NEW_POLICY_NAME_AFTER_TASK_COMPLETION = "myNewPolicyNameAfterTaskCompletion";
    private static Logger log = Logger.getLogger("ProcessInstanceLifecycleCustomComposer");

    @Override
    public Message compose(RESTEasyBindingData source, Exchange exchange) throws Exception {
        final Message message = super.compose(source, exchange);
        log.debug("compose() source = "+source);
        return message;
    }

    @Override
    public RESTEasyBindingData decompose(Exchange exchange, RESTEasyBindingData target) throws Exception {
        String opName = exchange.getContract().getProviderOperation().getName();
        target = super.decompose(exchange, target);
       
        if(TaskLifecycleResource.COMPLETE_TASK.equals(opName)) {
            ProcessDetails pDetails = (ProcessDetails)exchange.getMessage().getContent();
            log.debug("decompose() opName = "+opName+" : pDetails = "+pDetails);
            Policy policyObj = getPolicy(pDetails);
            target.setParameters(new Object[]{pDetails.getTaskId(), policyObj.getPolicyId()+INTEGER_FLAG, policyObj.getPolicyName()});
        }
        return target;
    }
    
    private Policy getPolicy(ProcessDetails pDetails) throws Exception {
        String policyString = pDetails.getPayload();
        JAXBContext jaxbContext = JAXBContext.newInstance(Policy.class);
        Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
        StringReader sReader = new StringReader(policyString);
        Policy policyObj = (Policy)jaxbUnMarshaller.unmarshal(sReader);
        policyObj.setPolicyName(MY_NEW_POLICY_NAME_AFTER_TASK_COMPLETION);
        return policyObj;
    }
    
    private String getCommandRequestPayload(ProcessDetails pDetails) throws Exception {
        return null;
    }

}
