package com.gaming.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class GamingApiGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(GamingApiGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GamingApiGatewayApplication.class, args);
        logger.info("🎮 Gaming API Gateway started successfully on port 8080!");
        logger.info("📊 Health check available at: http://localhost:8080/actuator/health");
        logger.info("🔗 Gateway routes available at: http://localhost:8080/actuator/gateway/routes");
    }
}