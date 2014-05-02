package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.core.Response;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;
import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

public interface ProcessInstanceLifecycle {
	
    public String startProcessRest(ProcessDetails pDetails) throws Exception;
    public String startProcessExecutor(ProcessDetails pDetails) throws Exception;

}
