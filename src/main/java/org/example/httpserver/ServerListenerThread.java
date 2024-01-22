package org.example.httpserver;

import org.example.httpserver.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    private final Configuration configuration;

    public ServerListenerThread(Configuration configuration) {
        this.configuration = configuration;
    }

    public void run() {
        try (var serverSocket = new ServerSocket(configuration.port())) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new HttpConnectionHandlerThread(clientSocket, configuration.webroot())).start();
            }
        } catch (IOException e) {
            LOGGER.info("Server could not start: " + e.getMessage());
        }
    }
}
