package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

@Service(PolicyQuoteMgmt.class)
public class PolicyQuoteMgmtBean implements PolicyQuoteMgmt {
    
    private static Logger log = Logger.getLogger("PolicyQuoteMgmtBean");

    public void postPolicy(Policy pObj) {
        log.info("setFinalQuotePrice() policyId = "+pObj.getPolicyId()+" : final quote price = "+pObj.getPolicyName());
    }

}
