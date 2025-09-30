package com.gaming.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestResponseLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Log de la petición entrante
        String timestamp = LocalDateTime.now().format(formatter);
        String method = request.getMethod().toString();
        String path = request.getPath().toString();
        String clientIp = getClientIp(request);

        logger.info("🔄 [{}] {} {} from {} - Processing request",
                timestamp, method, path, clientIp);

        // Procesar la petición y log de la respuesta
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    int statusCode = response.getStatusCode() != null ?
                            response.getStatusCode().value() : 0;

                    String statusEmoji = getStatusEmoji(statusCode);

                    logger.info("{} [{}] {} {} - Status: {} ({}ms)",
                            statusEmoji,
                            LocalDateTime.now().format(formatter),
                            method,
                            path,
                            statusCode,
                            System.currentTimeMillis() - getStartTime(exchange));
                }));
    }

    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddress() != null ?
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }

    private String getStatusEmoji(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return "✅";
        if (statusCode >= 300 && statusCode < 400) return "🔄";
        if (statusCode >= 400 && statusCode < 500) return "⚠️";
        if (statusCode >= 500) return "❌";
        return "❓";
    }

    private long getStartTime(ServerWebExchange exchange) {
        Long startTime = exchange.getAttribute("startTime");
        if (startTime == null) {
            startTime = System.currentTimeMillis();
            exchange.getAttributes().put("startTime", startTime);
        }
        return startTime;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}