package io.github.zforgo.jsr375.jwt;

import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class JwtDynamicFeature implements DynamicFeature {

	@Inject
	private JwtManager jwtManager;

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtManager);
		if (!isPublicResourceAnnotationPresent(resourceInfo)) {
			context.register(jwtAuthFilter, Priorities.AUTHENTICATION);
		}
	}

	private boolean isPublicResourceAnnotationPresent(ResourceInfo info) {
		return info.getResourceClass().getAnnotation(PublicResource.class) != null ||
				info.getResourceMethod().getAnnotation(PublicResource.class) != null;
	}
}
