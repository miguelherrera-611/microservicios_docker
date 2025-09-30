package com.gaming.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableR2dbcAuditing
public class GamingUserServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(GamingUserServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GamingUserServiceApplication.class, args);
        logger.info("Gaming User Service started successfully on port 8082!");
        logger.info("Health check available at: http://localhost:8082/actuator/health");
        logger.info("API endpoints available at: http://localhost:8082/api/users");
        logger.info("API Documentation: http://localhost:8082/api/users (GET, POST, PUT, DELETE)");
    }
}