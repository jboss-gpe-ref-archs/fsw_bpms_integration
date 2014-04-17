package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.apache.log4j.Logger;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;

@Service(ProcessMgmt.class)
public class ProcessMgmtBean implements ProcessMgmt {

    private static Logger log = Logger.getLogger("ProcessMgmtBean");
    
    @Inject @Reference("StartProcess")
    private StartProcess sProcessAction;
    
    @Inject @Reference("GetPotentialTasks")
    private GetPotentialTasks gPotentialTasks;

    public void executeProcessLifecycle(ProcessDetails pDetails) {
        log.info("executeProcessLifecycle() pDetails = "+pDetails);
        
        sProcessAction.start();
        log.info("executeProcessLifecycle() invoking getPotentialTasks()");
        gPotentialTasks.getPotentialTasks();
    }
}
