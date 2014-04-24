package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.Policy;

@Path("/{deploymentId}")
public interface ProcessInstanceLifecycleResource {
	
    @POST
    @Path("/process/{processId}/start")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/x-www-form-urlencoded")
    public String startProcess(@PathParam("deploymentId") String deploymentId, 
    		                   @PathParam("processId") String processId, 
    		                   @FormParam("policy") String policyJaxb) throws Exception;
    
}
