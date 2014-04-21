package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.core.Response;

public interface TaskLifecycle {
	
	public String queryForPotentialTasks(String groupId) throws Exception;
    public Response claimTask(String taskId) throws Exception;
    public Response startTask(String taskId) throws Exception;
    public Response completeTask(String taskId) throws Exception;

}
