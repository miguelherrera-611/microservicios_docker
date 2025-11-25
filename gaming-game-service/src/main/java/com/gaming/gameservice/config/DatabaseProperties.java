package com.gaming.gameservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.database")
public class DatabaseProperties {

    /**
     * Whether schema.sql should run on startup.
     */
    private boolean initialize = false;

    /**
     * Classpath location for schema script.
     */
    private String schemaLocation = "schema.sql";

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }
}

