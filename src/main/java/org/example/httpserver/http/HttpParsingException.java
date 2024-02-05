package org.example.httpserver.http;

public class HttpParsingException extends Exception {

    private final HttpStatusCode errorCode;

    public HttpParsingException(HttpStatusCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
