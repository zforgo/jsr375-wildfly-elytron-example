package io.github.zforgo.jsr375.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class JwtManager {

	@Inject
	@JwtKey
	private Key jwtSecret;

	private static final int EXP_HOURS = 10;
	private static final String ROLES_CLAIM = "roles";

	public String createJwtToken(String subject, Set<String> roles) {
		return Jwts.builder()
				.setSubject(subject)
				.claim(ROLES_CLAIM, roles)
				.setIssuedAt(new Date())
				.setExpiration(Date.from(ZonedDateTime.now().plusHours(EXP_HOURS).toInstant()))
				.signWith(jwtSecret)
				.compact();
	}

	UserPrincipal parse(String token) {
		Keys.secretKeyFor(SignatureAlgorithm.HS512);
		Jws<Claims> jwt = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
		String subject = jwt.getBody().getSubject();

		Set<String> roles = Optional.ofNullable(jwt.getBody().get("roles"))
				.filter(r -> r instanceof Collection)
				.map(r -> (Collection<Object>) r)
				.map(rc -> rc.stream().map(Object::toString).collect(Collectors.toSet()))
				.orElse(Set.of());
		return new UserPrincipal(subject, roles);
	}
}
