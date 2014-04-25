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

    public static final String START_PROCESS_REST = "startProcessRest";
    public static final String START_PROCESS_EXECUTOR = "startProcessExecutor";
	
    @POST
    @Path("/process/{processId}/start")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/x-www-form-urlencoded")
    public String startProcessRest(@PathParam("deploymentId") String deploymentId, 
    		                   @PathParam("processId") String processId, 
    		                   @FormParam("policy") String map_payload) throws Exception;

    @POST
    @Path("/execute")
    public String startProcessExecutor(@FormParam("payload") String executor_request_payload) throws Exception; 
    
}
