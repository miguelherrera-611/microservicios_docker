package com.gaming.gameservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableR2dbcAuditing
public class GamingGameServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(GamingGameServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GamingGameServiceApplication.class, args);
        logger.info("🎮 Gaming Game Service started successfully on port 8081!");
        logger.info("📊 Health check available at: http://localhost:8081/actuator/health");
        logger.info("🔗 API endpoints available at: http://localhost:8081/api/games");
        logger.info("📚 API Documentation: http://localhost:8081/api/games (GET, POST, PUT, DELETE)");
    }
}