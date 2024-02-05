package org.example.httpserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public HttpMessage parseHttpRequest(InputStream inputStream) throws IOException {
        LOGGER.info("Parsing HTTP request");

        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var request = new HttpRequest();

        parseRequestLine(reader,request);
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(BufferedReader reader, HttpRequest request) throws IOException {
        String requestLine = reader.readLine();
        LOGGER.info("Request line: " + requestLine);
        String[] requestLineParts = requestLine.split(" ");
        request.setMethod(requestLineParts[0]);
        request.setUri(requestLineParts[1]);
        request.setHttpVersion(requestLineParts[2]);
    }

    private void parseHeaders(BufferedReader reader, HttpRequest request) throws IOException {
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            LOGGER.info("Header: " + headerLine);
            String[] headerParts = headerLine.split(": ");
            request.addHeader(headerParts[0], headerParts[1]);
        }
    }

    private void parseBody(BufferedReader reader, HttpRequest request) throws IOException {
        if (reader.ready()) {
            String body = reader.lines().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
            LOGGER.info("Body: " + body);
            request.setBody(body);
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

}
