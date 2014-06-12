package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.FormParam;

@Path("/")
public interface PolicyQuoteMgmtResource {
    
    @POST
    @Path("policy")
    public void postPolicy(@FormParam("payload") String payload) throws Exception;

}
