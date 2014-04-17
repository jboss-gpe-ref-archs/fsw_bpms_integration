package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.io.StringWriter;
import java.util.Random;
import javax.inject.Inject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.apache.log4j.Logger;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;
import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

@Service(ProcessMgmt.class)
public class ProcessMgmtBean implements ProcessMgmt {

    private static Logger log = Logger.getLogger("ProcessMgmtBean");
    private static Random random = new Random();
    
    @Inject @Reference("StartProcess")
    private StartProcess sProcessAction;
    
    @Inject @Reference("GetPotentialTasks")
    private GetPotentialTasks gPotentialTasks;

    public void executeProcessLifecycle(ProcessDetails pDetails) {
        log.info("executeProcessLifecycle() pDetails = "+pDetails);
      
        try { 
            String policyJaxb = getPolicyJaxb(); 
            String response = sProcessAction.start(policyJaxb);
            log.info("executeProcessLifecycle() startProcess response = "+response);

            //gPotentialTasks.getPotentialTasks();
        }catch(Throwable x) {
            x.printStackTrace();
        }
    }

    private String getPolicyJaxb() throws JAXBException {
        Policy policyObj = new Policy(random.nextInt(1000));
        JAXBContext jaxbContext = JAXBContext.newInstance(Policy.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(policyObj, sw);
        return sw.toString();
    }
}
