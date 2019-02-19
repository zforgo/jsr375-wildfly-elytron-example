package io.github.zforgo.jsr375.jwt;

import io.jsonwebtoken.security.Keys;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.security.Key;

@ApplicationScoped
public class JwtKeyProvider {
	private final Key key = Keys.hmacShaKeyFor("LoenIKtbPGQMBbwNgMooQnsyrmADdfSTvkXUDcUUIOLQhMUKVG".getBytes());

	@Produces
	@JwtKey
	public Key getKey() {
		return key;
	}
}
