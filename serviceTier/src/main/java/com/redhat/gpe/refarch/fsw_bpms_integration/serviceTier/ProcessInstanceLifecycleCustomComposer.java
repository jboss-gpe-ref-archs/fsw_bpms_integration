package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.component.common.label.EndpointLabel;
import org.switchyard.component.resteasy.composer.RESTEasyBindingData;
import org.switchyard.component.resteasy.composer.RESTEasyContextMapper;
import org.switchyard.component.resteasy.composer.RESTEasyMessageComposer;
import org.apache.log4j.Logger;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

public class ProcessInstanceLifecycleCustomComposer extends RESTEasyMessageComposer {
	
	private static final String MAP_POLICY = "map_policy=";
	private static Logger log = Logger.getLogger("ProcessInstanceLifecycleCustomComposer");

    @Override
    public Message compose(RESTEasyBindingData source, Exchange exchange) throws Exception {
        final Message message = super.compose(source, exchange);
        log.info("compose() source = "+source);
        return message;
    }

    @Override
    public RESTEasyBindingData decompose(Exchange exchange, RESTEasyBindingData target) throws Exception {
        ProcessDetails pDetails = (ProcessDetails)exchange.getMessage().getContent();
        String opName = exchange.getContract().getProviderOperation().getName();
        log.info("decompose() opName = "+opName+" : pDetails = "+pDetails);
        
        target = super.decompose(exchange, target);
        
        target.setParameters(new Object[]{pDetails.getDeploymentId(), pDetails.getProcessId(), pDetails.getPolicyJaxb()});

        return target;
    }

}
