package com.gaming.reviewservice.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class DatabaseConfig {

    @Bean
    @ConditionalOnProperty(prefix = "spring.database", name = "initialize", havingValue = "true")
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory, DatabaseProperties databaseProperties) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ClassPathResource(databaseProperties.getSchemaLocation())
        );
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}