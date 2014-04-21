package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.core.Response;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

public interface ProcessInstanceLifecycle {
	
    public String startProcess(String processId) throws Exception;

}
