package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

public interface ProcessMgmt {

    // https://access.redhat.com/site/documentation/en-US/Red_Hat_JBoss_BPM_Suite/6.0/html-single/Development_Guide/index.html#sect-Runtime_calls
    void executeProcessLifecycleViaRest(ProcessDetails x);

    // https://access.redhat.com/site/documentation/en-US/Red_Hat_JBoss_BPM_Suite/6.0/html-single/Development_Guide/index.html#Execute_calls
    void executeProcessLifecycleViaExecutionAPI(ProcessDetails pDetails);

}
