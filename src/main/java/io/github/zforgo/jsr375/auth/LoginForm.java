package io.github.zforgo.jsr375.auth;

import javax.ws.rs.FormParam;
import java.io.Serializable;

public class LoginForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@FormParam("username")
	private String username;

	@FormParam("password")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
