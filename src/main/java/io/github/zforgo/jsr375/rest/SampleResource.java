package io.github.zforgo.jsr375.rest;

import io.github.zforgo.jsr375.jwt.PublicResource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/sample")
@Produces(MediaType.TEXT_PLAIN)
public class SampleResource {

	@Context
	private SecurityContext sc;

	@PublicResource
	@Path("/echo")
	public Response echo() {
		return Response
				.ok("Hello: " + (null != sc.getUserPrincipal() ? sc.getUserPrincipal().getName() : "Anonymous"))
				.build();

	}

	@Path("/isAdmin")
	public Response isAdmin() {
		return Response
				.ok(sc.isUserInRole("ADMIN"))
				.build();

	}

	@Path("/manager")
	@RolesAllowed("MANAGER")
	public Response hasRole() {
		return Response.ok("M'Lord").build();
	}
}
