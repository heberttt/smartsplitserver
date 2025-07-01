package com.SmartSplit.ApiGateway.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; // For WebFlux/Gateway
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Use this for Spring Cloud Gateway (WebFlux based)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for stateless APIs
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/public/**").permitAll() // Example: public paths
                .anyExchange().authenticated() // All other requests require authentication
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {
                    // These properties are typically picked up from application.yml,
                    // but you can configure them explicitly here if needed.
                    // jwt.jwkSetUri("https://www.googleapis.com/service_accounts/v1/jwk/securetoken%40system.gserviceaccount.com");
                    // jwt.issuerUri("https://securetoken.google.com/smartsplit-87a0b");
                })
            );
        return http.build();
    }
}