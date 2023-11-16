package org.example.httpserver;

import org.example.httpserver.config.Configuration;
import org.example.httpserver.config.ConfigurationManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) {

        System.out.println("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("http.json");
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

        try (var serverSocket = new ServerSocket(configuration.port())) {
            System.out.println("Server started on port " + configuration.port() + " and webroot " + configuration.webroot());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, configuration.webroot())).start();
            }

        } catch (IOException e) {
            System.out.println("Server could not start: " + e.getMessage());
        }

        System.out.println("Server started on port " + configuration.port() + " and host " + configuration.webroot());

    }
}
