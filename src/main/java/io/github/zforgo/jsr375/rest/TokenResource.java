package io.github.zforgo.jsr375.rest;

import io.github.zforgo.jsr375.auth.LoginForm;
import io.github.zforgo.jsr375.jwt.JwtManager;
import io.github.zforgo.jsr375.jwt.PublicResource;
import io.github.zforgo.jsr375.jwt.Roles;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Path("/login")
public class TokenResource {

	@Inject
	private JwtManager jwtManager;

	@POST
	@PublicResource
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response login(@NotNull @BeanParam LoginForm form, @Context HttpServletRequest request) {
		try {
			if (request.getUserPrincipal() != null) {
				request.logout();
			}
			request.login(form.getUsername(), form.getPassword());
			Set<String> roles = Arrays.stream(Roles.values())
					.map(Enum::name)
					.filter(request::isUserInRole)
					.collect(Collectors.toSet());
			String login = request.getUserPrincipal().getName();
			request.getSession().invalidate();
			return Response.ok(jwtManager.createJwtToken(login, roles)).type(MediaType.TEXT_PLAIN_TYPE).build();
		} catch (ServletException e) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity("WRONG_CREDENTIALS ".concat("Authentication failed, wrong credentials"))
					.type(MediaType.TEXT_PLAIN)
					.build();
		}
	}
}
