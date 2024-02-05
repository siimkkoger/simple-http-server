package org.example.httpserver;

import org.example.httpserver.http.HttpMessage;
import org.example.httpserver.http.HttpMethod;
import org.example.httpserver.http.HttpParser;
import org.example.httpserver.http.HttpParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HttpConnectionHandlerThread implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionHandlerThread.class);
    private final Socket socket;
    private final String webroot;
    private final HttpParser httpParser;

    public HttpConnectionHandlerThread(Socket socket, String webRoot) {
        this.socket = socket;
        this.webroot = webRoot;
        this.httpParser = new HttpParser();
    }

    @Override
    public void run() {
        try (socket;
             var input = socket.getInputStream();
             var output = socket.getOutputStream();
             var writer = new BufferedWriter(new OutputStreamWriter(output))) {

            HttpMessage request = httpParser.parseHttpRequest(input);
            System.out.println(request);
            String response = processRequest("wefwefwefwef");
            sendResponse(writer, response);

        } catch (IOException | HttpParsingException e) {
            LOGGER.error("Error handling client connection", e);
        }
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
