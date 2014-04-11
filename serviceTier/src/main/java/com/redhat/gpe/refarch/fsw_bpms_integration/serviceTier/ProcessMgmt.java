package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

public interface ProcessMgmt {

    void executeProcessLifecycle(ProcessDetails x);

}
