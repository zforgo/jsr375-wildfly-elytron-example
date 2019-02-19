package io.github.zforgo.jsr375;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class LoginIT extends ArquillianRestBaseIT {


	@Test
	public void loginWithoutCredentials() {
		given().log().all()
				.when()
				.post(resolveApiUrl("/login"))
				.then()
				.log().all()
				.statusCode(401);
	}

	@Test
	public void loginWithBadCredentials() {
		given().log().all()
				.when()
				.formParam("username", "foo")
				.formParam("password", "bar")
				.post(resolveApiUrl("/login"))
				.then()
				.log().all()
				.statusCode(401);
	}

	@Test
	public void successfulLogin() {
		String jwtToken = given().log().all()
				.when()
				.formParam("username", "user1")
				.formParam("password", "password")
				.post(resolveApiUrl("/login"))
				.then()
				.log().all()
				.statusCode(200).extract().body().asString();
		int jwtPartsCount = jwtToken.split("\\.").length;
		assertEquals("The JWT token should have 3 parts", 3, jwtPartsCount);
	}
}
