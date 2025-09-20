//package com.pm.apigateway.filter;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Component
//public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
//
//	private final WebClient webClient;
//
//	public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
//			@Value("${auth.service.url}") String authServiceUrl) {
//
//		// depending on the ENV end point changes
//		// auth-service:4005 on the docker
//		// ecs.aws.askdjkas:5000 on the amazon
//
//		this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
//	}
//
//	@Override
//	public GatewayFilter apply(Object config) {
//		return (exchange, chain) -> {
//			String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//			if (token == null || !token.startsWith("Bearer ")) {
//				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//				return exchange.getResponse().setComplete();
//			}
//
//			return webClient.get().uri("/validate").header(HttpHeaders.AUTHORIZATION, token).retrieve()
//					.toBodilessEntity() // return void no body to response
//					// our filter complete the processing the request all was successful and
//					// continue request
//					.then(chain.filter(exchange)); // If success response then continue chain
//
//		};
//	}
//
//}

package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	private final WebClient webClient;

	public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
			@Value("${auth.service.url}") String authServiceUrl) {
		this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

			if (token == null || !token.startsWith("Bearer ")) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			return webClient.get().uri("/validate").header(HttpHeaders.AUTHORIZATION, token).retrieve()
					.toBodilessEntity().then(chain.filter(exchange));
		};
	}
}
