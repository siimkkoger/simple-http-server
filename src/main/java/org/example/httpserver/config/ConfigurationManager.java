package org.example.httpserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ConfigurationManager {

    private static ConfigurationManager instance;
    private static Configuration configuration;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void loadConfigurationFile(String resourcePath) {
        // Use class loader to get the resource input stream
        var inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new HttpConfigurationException("Resource not found: " + resourcePath);
        }

        var objectMapper = new ObjectMapper();
        try {
            configuration = objectMapper.readValue(inputStream, Configuration.class);
        } catch (IOException e) {
            throw new HttpConfigurationException("Error while parsing configuration file: " + e.getMessage());
        }
    }

    public Configuration getCurrentConfiguration() {
        if (configuration == null) {
            throw new HttpConfigurationException("Configuration not loaded");
        }
        return configuration;
    }
}
