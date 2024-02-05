package org.example.httpserver.http;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD;

    public static final int MAX_LENGTH;

    static {
        int max = 0;
        for (HttpMethod method : values()) {
            if (method.name().length() > max) {
                max = method.name().length();
            }
        }
        MAX_LENGTH = max;
    }
}
