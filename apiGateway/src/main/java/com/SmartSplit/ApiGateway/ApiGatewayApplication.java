package com.SmartSplit.ApiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/accounts/**")
						.filters(f -> f
							.prefixPath("/api")
							.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8081")
				)
				.route(r -> r.path("/account2/**")
						.filters(f -> f
							.prefixPath("/public")
							.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8081")
				)
				.route(r -> r.path("/friendRequest/**")
						.filters(f -> f
							.prefixPath("/api")
							.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8081")
				)
				.route(r -> r.path("/friends/**")
						.filters(f -> f
							.prefixPath("/api")
							.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8081")
				)
				.route(r -> r.path("/groups/**")
						.filters(f -> f
							.prefixPath("/api")
							.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8081")
				)
				.route(r -> r.path("/auth/**")
						.filters(f -> f
								.prefixPath("/api")
								.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8082")
				)
				.route(r -> r.path("/dataProcess/**")
						.filters(f -> f
								.prefixPath("/api")
								.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8083")
				)
				.route(r -> r.path("/splits/**")
						.filters(f -> f
								.prefixPath("/api")
								.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8084")
				)
				.route(r -> r.path("/debts/**")
						.filters(f -> f
								.prefixPath("/api")
								.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8084")
				)
				.route(r -> r.path("/splitbill/**")
						.filters(f -> f
								.prefixPath("/public")
								.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8084")
				)
				.route(r -> r.path("/ocr/**")
						.filters(f -> f
								.prefixPath("/api")
								.addResponseHeader("X-Powered-By","SmartSplit Gateway Service")
						)
						.uri("http://localhost:8000")
				)
				.build();
	}

}
