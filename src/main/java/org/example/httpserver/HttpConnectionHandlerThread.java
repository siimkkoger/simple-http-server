package org.example.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class HttpConnectionHandlerThread implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionHandlerThread.class);
    private final Socket socket;
    private final String webroot;

    public HttpConnectionHandlerThread(Socket socket, String webRoot) {
        this.socket = socket;
        this.webroot = webRoot;
    }

    @Override
    public void run() {
        try (socket;
             var input = socket.getInputStream();
             var output = socket.getOutputStream();
             var reader = new BufferedReader(new InputStreamReader(input));
             var writer = new BufferedWriter(new OutputStreamWriter(output))) {

            String request = parseRequest(reader);
            System.out.println(request);
            String response = processRequest(request);
            sendResponse(writer, response);

        } catch (IOException e) {
            LOGGER.error("Error handling client connection", e);
        }
    }

    private String parseRequest(BufferedReader reader) throws IOException {
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\n");
        }
        return requestBuilder.toString();
    }

    private String processRequest(String request) throws IOException {
        // Example of simple request processing. This should be expanded
        // based on your application's requirements.
        if (request.contains("GET / HTTP/1.1")) {
            return getHtmlContent(webroot + "/welcome.html");
        } else {
            return "<html><body><h1>Error 500: Internal Server Error</h1></body></html>";
        }
    }

    private void sendResponse(BufferedWriter writer, String response) throws IOException {
        final var CRLF = "\r\n";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        var httpResponse = "HTTP/1.1 200 OK" + CRLF +
                "Content-Type: text/html; charset=UTF-8" + CRLF +
                "Content-Length: " + responseBytes.length + CRLF +
                "Connection: close" + CRLF +
                CRLF +
                response;
        writer.write(httpResponse);
        writer.flush();
    }

    private String getHtmlContent(String filePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found");
        }
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }
}
