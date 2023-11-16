package org.example.httpserver;

import org.example.httpserver.config.Configuration;
import org.example.httpserver.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {

        LOGGER.info("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("http.json");
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();
        new ServerListenerThread(configuration).start();

        LOGGER.info("Server started on port " + configuration.port() + " and host " + configuration.webroot());
    }
}
