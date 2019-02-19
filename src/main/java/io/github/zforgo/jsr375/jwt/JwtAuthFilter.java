package io.github.zforgo.jsr375.jwt;

import io.jsonwebtoken.JwtException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

	private JwtManager jwtManager;

	private static final String BEARER = "Bearer ";
	private static final Logger LOG = Logger.getLogger(JwtAuthFilter.class.getName());

	public JwtAuthFilter(JwtManager jwtManager) {
		this.jwtManager = jwtManager;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {
		String token = extractToken(requestContext);
		if (token == null) {
			requestContext.abortWith(createJwtErrorResponse("The JWT token is missing"));
			return;
		}

		try {
			UserPrincipal userPrincipal = jwtManager.parse(token);
			requestContext.setSecurityContext(new JwtSecurityContext(userPrincipal, requestContext.getSecurityContext().isSecure()));
		} catch (JwtException ex) {
			LOG.log(Level.FINE, "JWT token was not accepted", ex);
			requestContext.abortWith(createJwtErrorResponse(ex.getMessage()));
		}
	}

	private String extractToken(ContainerRequestContext context) {
		String header = context.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith(BEARER)) {
			return header.substring(BEARER.length());
		}
		return null;
	}

	private Response createJwtErrorResponse(String message) {
		return Response
				.status(Response.Status.UNAUTHORIZED)
				.entity("JWT_TOKEN_ERROR ".concat(message))
				.type(MediaType.TEXT_PLAIN)
				.build();
	}
}
