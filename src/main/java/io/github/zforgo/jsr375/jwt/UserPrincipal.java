package io.github.zforgo.jsr375.jwt;

import java.security.Principal;
import java.util.Set;

public class UserPrincipal implements Principal {

	private String login;
	private Set<String> roles;

	UserPrincipal(String login, Set<String> roles) {
		this.login = login;
		this.roles = roles;
	}

	@Override
	public String getName() {
		return login;
	}

	public boolean hasRole(String role) {
		return roles != null && roles.contains(role);
	}
}
