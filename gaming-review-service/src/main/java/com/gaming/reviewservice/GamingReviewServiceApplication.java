package com.gaming.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableR2dbcAuditing
public class GamingReviewServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(GamingReviewServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GamingReviewServiceApplication.class, args);
        logger.info("Gaming Review Service started successfully on port 8083!");
        logger.info("Health check available at: http://localhost:8083/actuator/health");
        logger.info("API endpoints available at: http://localhost:8083/api/reviews");
        logger.info("API Documentation: http://localhost:8083/api/reviews (GET, POST, PUT, DELETE)");
    }
}