package io.github.zforgo.jsr375.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/sample")
@Produces(MediaType.TEXT_PLAIN)
@Stateless
@PermitAll
public class SampleResource {

	@Context
	private SecurityContext sc;

	@Path("/echo")
	@GET
	public Response echo() {
		return Response
				.ok("Hello: " + sc.getUserPrincipal().getName())
				.build();

	}

	@Path("/isAdmin")
	@GET
	public Response isAdmin() {
		return Response
				.ok(sc.isUserInRole("ADMIN"))
				.build();

	}

	@Path("/manager")
	@RolesAllowed("MANAGER")
	@GET
	public Response hasRole() {
		return Response.ok("M'Lord").build();
	}
}
