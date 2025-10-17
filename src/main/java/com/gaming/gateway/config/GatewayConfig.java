package com.gaming.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Value("${GAME_SERVICE_URL:http://localhost:8081}")
    private String gameServiceUrl;

    @Value("${USER_SERVICE_URL:http://localhost:8082}")
    private String userServiceUrl;

    @Value("${REVIEW_SERVICE_URL:http://localhost:8083}")
    private String reviewServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Game Service Routes
                .route("game-service", r -> r.path("/api/games/**")
                        .uri(gameServiceUrl))

                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .uri(userServiceUrl))

                // Review Service Routes
                .route("review-service", r -> r.path("/api/reviews/**")
                        .uri(reviewServiceUrl))

                // Health checks
                .route("game-service-health", r -> r.path("/health/games")
                        .uri(gameServiceUrl + "/actuator/health"))

                .route("user-service-health", r -> r.path("/health/users")
                        .uri(userServiceUrl + "/actuator/health"))

                .route("review-service-health", r -> r.path("/health/reviews")
                        .uri(reviewServiceUrl + "/actuator/health"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}