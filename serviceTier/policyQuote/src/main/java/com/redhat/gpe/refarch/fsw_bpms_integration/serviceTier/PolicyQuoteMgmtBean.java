package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.inject.Inject;
import org.switchyard.Context;
import org.switchyard.component.bean.Service;
import org.apache.log4j.Logger;

@Service(PolicyQuoteMgmt.class)
public class PolicyQuoteMgmtBean implements PolicyQuoteMgmt {

    private static Logger log = Logger.getLogger("PolicyQuoteMgmtBean");
    
    @Inject
    private Context context;

    public PolicyQuoteMgmtBean() {
    }
	public void managePolicyQuote(String signal) {
		log.info("managePolicyQuote() signal = "+signal);
	}
}
