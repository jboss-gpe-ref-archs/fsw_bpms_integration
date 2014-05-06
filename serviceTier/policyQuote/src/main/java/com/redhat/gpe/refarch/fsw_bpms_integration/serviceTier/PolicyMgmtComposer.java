package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.component.resteasy.composer.RESTEasyBindingData;
import org.switchyard.component.resteasy.composer.RESTEasyMessageComposer;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

public class PolicyMgmtComposer extends RESTEasyMessageComposer{
	
    private Logger log = Logger.getLogger("PolicyMgmtComposer");
    private ObjectMapper jsonMapper = new ObjectMapper();
	
	public Message compose(RESTEasyBindingData source, Exchange exchange) throws Exception {
        final Message message = super.compose(source, exchange);
        String content = null;
        try {
        	content = (String)message.getContent();
        	Policy pObj = jsonMapper.readValue(content, Policy.class);
        	message.setContent(pObj);
        	return message;
        }catch(Exception x){
        	log.error("compose() content = "+message.getContent()+" : exception = "+x.getLocalizedMessage());
        	throw x;
        }
    }

}
