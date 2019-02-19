package io.github.zforgo.jsr375;

import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.ResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URL;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

class ArquillianRestBaseIT {

	private static String jwtToken;

	@ArquillianResource
	static URL arquillianURL;

	private static Supplier<File[]> dependencies = () -> Stream.of(
			Maven.configureResolver()
					.loadPomFromFile("pom.xml")
					.importRuntimeDependencies()
					.resolve().withTransitivity()
					.asResolvedArtifact())
			.filter(a -> !a.getCoordinate().getGroupId().startsWith("hu.virgo.dm"))
			.map(ResolvedArtifact::asFile)
			.toArray(File[]::new);

	private static Supplier<WebArchive> fullArchive = () -> ShrinkWrap.create(WebArchive.class, "api.war")
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
			.addPackages(true, "io.github.zforgo")
			.addAsLibraries(dependencies.get());

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fullArchive.get();
	}

	public static String resolveApiUrl(String... parts) {
		UriBuilder uriBuilder = UriBuilder.fromUri(arquillianURL.toString());
		for (String part : parts) {
			uriBuilder.path(part);
		}
		return uriBuilder.toTemplate();
	}

	public static RequestSpecification baseRequest() {
		return given().log().all(true)
				.filter(new ResponseLoggingFilter())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_PLAIN);
	}

	public static RequestSpecification authenticatedRequest() {
		if (jwtToken == null) {
			login();
		}

		return baseRequest()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
	}

	private static void login() {
		Response response = baseRequest()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("username", "user1")
				.formParam("password", "password")
				.accept(MediaType.TEXT_PLAIN)
				.post(resolveApiUrl("/login"))
				.then()
				.extract().response();
		assertEquals("The login attempt failed", 200, response.statusCode());
		jwtToken = response.body().asString();
	}
}
