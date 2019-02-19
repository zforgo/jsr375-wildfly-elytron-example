package io.github.zforgo.jsr375;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class SampleIT extends ArquillianRestBaseIT {

	@Test
	public void securedEcho() {
		var echoResponse = authenticatedRequest().get(resolveApiUrl("sample", "echo"))
				.then().log().all()
				.statusCode(200)
				.extract().body().asString();
		assertEquals("Response should be Anonymous", "Hello: user1", echoResponse);
	}

	@Test
	public void nonAdmin() {
		var isAdmin = authenticatedRequest().get(resolveApiUrl("sample", "isAdmin"))
				.then().log().all()
				.statusCode(200)
				.extract().body().asString();
		assertFalse("Logged in user mustn't be admin", Boolean.valueOf(isAdmin));
	}

	@Test
	public void nonManager() {
		authenticatedRequest().get(resolveApiUrl("sample", "manager"))
				.then().log().all()
				.statusCode(403);
	}
}
