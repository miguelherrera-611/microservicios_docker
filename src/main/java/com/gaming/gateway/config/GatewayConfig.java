package com.gaming.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    @Value("${services.game.url}")
    private String gameServiceUrl;

    @Value("${services.user.url}")
    private String userServiceUrl;

    @Value("${services.review.url}")
    private String reviewServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        logger.info("🔧 Configurando rutas del Gateway:");
        logger.info("  📦 Game Service URL: {}", gameServiceUrl);
        logger.info("  👤 User Service URL: {}", userServiceUrl);
        logger.info("  ⭐ Review Service URL: {}", reviewServiceUrl);

        return builder.routes()
                // Game Service Routes
                .route("game-service", r -> r
                        .path("/api/games/**")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                .addRequestHeader("X-Gateway", "Gaming-API-Gateway"))
                        .uri(gameServiceUrl))

                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                .addRequestHeader("X-Gateway", "Gaming-API-Gateway"))
                        .uri(userServiceUrl))

                // Review Service Routes
                .route("review-service", r -> r
                        .path("/api/reviews/**")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                .addRequestHeader("X-Gateway", "Gaming-API-Gateway"))
                        .uri(reviewServiceUrl))

                // Health checks para Game Service
                .route("game-service-health", r -> r
                        .path("/health/games")
                        .uri(gameServiceUrl + "/actuator/health"))

                // Health checks para User Service
                .route("user-service-health", r -> r
                        .path("/health/users")
                        .uri(userServiceUrl + "/actuator/health"))

                // Health checks para Review Service
                .route("review-service-health", r -> r
                        .path("/health/reviews")
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
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}