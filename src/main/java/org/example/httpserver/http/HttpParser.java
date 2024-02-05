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

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        LOGGER.info("Parsing HTTP request");

        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var request = new HttpRequest();

        parseRequestLine(reader,request);
        //parseHeaders(reader, request);
        //parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(BufferedReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == LF) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Request line is incomplete");
            }

            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    LOGGER.debug("Request line VERSION to process: {}", processingDataBuffer);
                    if (!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Request line is incomplete");
                    }

                    LOGGER.debug("Request line VERSION to process: {}", processingDataBuffer);
                    request.setHttpVersion(processingDataBuffer.toString());

                    break;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Request line is incomplete");
                }
            }

            if (_byte == SP) {
                if (!methodParsed) {
                    LOGGER.debug("Request line METHOD to process: {}", processingDataBuffer);
                    try {
                        request.setMethod(HttpMethod.valueOf(processingDataBuffer.toString()));
                    } catch (IllegalArgumentException e) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, "Unsupported HTTP method: " + processingDataBuffer);
                    }
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    LOGGER.debug("Request line REQUEST TARGET to process: {}", processingDataBuffer);
                    request.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, "Request line contains too many spaces");
                }
                LOGGER.debug("Processing data: {}", processingDataBuffer);
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char) _byte);
                if (!methodParsed) {
                    if (processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, "Request line method is too long");
                    }
                }
            }
        }
    }

}
