package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.component.resteasy.composer.RESTEasyBindingData;
import org.switchyard.component.resteasy.composer.RESTEasyMessageComposer;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

public class PolicyMgmtComposer extends RESTEasyMessageComposer{
	
	public Message compose(RESTEasyBindingData source, Exchange exchange) throws Exception {
        final Message message = super.compose(source, exchange);
        Policy pObj = new Policy(1, "2");
        message.setContent(pObj);
        return message;
    }

}
