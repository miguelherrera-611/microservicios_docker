package com.gaming.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Game Service Routes - Todas las rutas relacionadas con juegos
                .route("game-service", r -> r.path("/api/games/**")
                        .uri("http://localhost:8081"))

                // User Service Routes - Todas las rutas relacionadas con usuarios
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("http://localhost:8082"))

                // Review Service Routes - Todas las rutas relacionadas con reseñas
                .route("review-service", r -> r.path("/api/reviews/**")
                        .uri("http://localhost:8083"))

                // Health checks para cada servicio
                .route("game-service-health", r -> r.path("/health/games")
                        .uri("http://localhost:8081/actuator/health"))

                .route("user-service-health", r -> r.path("/health/users")
                        .uri("http://localhost:8082/actuator/health"))

                .route("review-service-health", r -> r.path("/health/reviews")
                        .uri("http://localhost:8083/actuator/health"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOriginPattern("*"); // En producción, especifica dominios exactos
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}