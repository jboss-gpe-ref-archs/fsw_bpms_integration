package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

@Path("/")
public interface TaskLifecycleResource {
	
	@GET
	@Path("/query?potentialOwner=")
	@Consumes("application/x-www-form-urlencoded")
	public String queryForPotentialTasks(@PathParam("groupId") String groupId) throws Exception;

    @POST
    @Path("/{taskId}/claim")
    public Response claimTask(@PathParam("taskId") String taskId) throws Exception;

    @POST
    @Path("/{taskId}/start")
    public Response startTask(@PathParam("taskId") String taskId) throws Exception;

    @POST
    @Path("/{taskId}/complete")
    public Response completeTask(@PathParam("taskId") String taskId) throws Exception;

}
