package org.example.httpserver;

import org.example.httpserver.config.Configuration;
import org.example.httpserver.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {

        LOGGER.info("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("http.json");
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

        try (var serverSocket = new ServerSocket(configuration.port())) {
            LOGGER.info("Server started on port " + configuration.port() + " and webroot " + configuration.webroot());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, configuration.webroot())).start();
            }

        } catch (IOException e) {
            LOGGER.info("Server could not start: " + e.getMessage());
        }

        LOGGER.info("Server started on port " + configuration.port() + " and host " + configuration.webroot());

    }
}
