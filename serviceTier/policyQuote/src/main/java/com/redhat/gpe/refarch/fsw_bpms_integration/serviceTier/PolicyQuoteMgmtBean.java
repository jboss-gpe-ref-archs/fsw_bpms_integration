package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

@Service(PolicyQuoteMgmt.class)
public class PolicyQuoteMgmtBean implements PolicyQuoteMgmt {
	
	private static Logger log = Logger.getLogger("PolicyQuoteMgmtBean");

	public void setFinalQuotePrice(String pInstanceId, String price) {
		log.info("setFinalQuotePrice() pInstanceId = "+pInstanceId+" : final quote price = "+price);
	}

}
