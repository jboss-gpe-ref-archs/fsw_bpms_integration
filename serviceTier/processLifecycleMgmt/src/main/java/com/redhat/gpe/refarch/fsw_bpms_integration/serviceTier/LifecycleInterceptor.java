package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.ExchangeInterceptor;
import org.switchyard.ExchangeState;
import org.switchyard.HandlerException;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceMetadata;
import org.apache.log4j.Logger;

@Named("LifecycleInterceptor")
public class LifecycleInterceptor implements ExchangeInterceptor {
    
    private static Logger log = Logger.getLogger("LifecycleInterceptor");
    
    public void before(String target, Exchange exchange) throws HandlerException {
    }
    
    public void after(String target, Exchange exchange) throws HandlerException {
        if(ExchangeState.FAULT.equals(exchange.getState())){
            String name = exchange.getProvider().getName().getLocalPart();
            String state = exchange.getState().name();
            ServiceDomain sDomain = exchange.getProvider().getDomain();
            ServiceMetadata sMetadata = exchange.getProvider().getServiceMetadata();
            ExchangeHandler eHandler = exchange.getProvider().getProviderHandler();
            log.error("after() providerName = "+name+" : state = "+state);
        }
    }
    
    public List<String> getTargets() {
           return Arrays.asList(PROVIDER);
    }

}
