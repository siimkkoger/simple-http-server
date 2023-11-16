package org.example.httpserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

class ClientHandler implements Runnable {

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
                //System.out.println(headerLine);
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
            System.out.println("Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
