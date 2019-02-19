package io.github.zforgo.jsr375.jwt;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JwtSecurityContext implements SecurityContext {

	private UserPrincipal principal;
	private boolean secure;


	public JwtSecurityContext(UserPrincipal principal, boolean secure) {
		this.principal = principal;
		this.secure = secure;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isUserInRole(String role) {
		return principal.hasRole(role);
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public String getAuthenticationScheme() {
		return "BEARER_TOKEN";
	}
}
