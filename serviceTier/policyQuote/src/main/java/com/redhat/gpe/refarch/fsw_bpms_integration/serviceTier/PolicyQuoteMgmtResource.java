package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/{pInstanceId}")
public interface PolicyQuoteMgmtResource {
	
	@POST
	@Path("{price}")
	void setFinalQuotePrice(@PathParam("pInstanceId") String pInstanceId, @PathParam("price") String price) throws Exception;

}
