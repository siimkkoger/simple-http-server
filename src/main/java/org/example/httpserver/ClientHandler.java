package org.example.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final Socket clientSocket;
    private final String webroot;

    public ClientHandler(Socket socket, String webRoot) {
        this.clientSocket = socket;
        this.webroot = webRoot;
    }

    private String readHtmlContent(String filePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/welcome.html");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found");
        }
        var htmlContent = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        return htmlContent;
    }

    @Override
    public void run() {
        try (var input = clientSocket.getInputStream();
             var output = clientSocket.getOutputStream();
             var reader = new BufferedReader(new InputStreamReader(input));
             var writer = new BufferedWriter(new OutputStreamWriter(output))) {

            // Read the HTTP request header
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                //LOGGER.info(headerLine);
            }

            // Read HTML content from file
            var htmlContent = readHtmlContent("xxx");


            // HTTP response header
            final var CRLF = "\r\n";
            var response = "HTTP/1.1 200 OK" + CRLF +
                    "Content-Type: text/html" + CRLF +
                    "Content-Length: " + htmlContent.getBytes().length + CRLF +
                    "Connection: close" + CRLF +
                    CRLF +
                    htmlContent;

            // Send the response
            writer.write(response);
            writer.flush();

        } catch (IOException e) {
            LOGGER.info("Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.info("Error closing socket: " + e.getMessage());
            }
        }
    }
}
