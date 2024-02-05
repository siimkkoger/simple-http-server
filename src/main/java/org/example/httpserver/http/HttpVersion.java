package org.example.httpserver.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern httpVersionRegex = Pattern.compile("HTTP/(?<major>\\d+)\\.(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws HttpParsingException {
        Matcher matcher = httpVersionRegex.matcher(literalVersion);

        if (!matcher.matches() || matcher.groupCount() != 2) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR, "Invalid HTTP version");
        }

        HttpVersion bestVersion = null;
        if (matcher.matches()) {
            int major = Integer.parseInt(matcher.group("major"));
            int minor = Integer.parseInt(matcher.group("minor"));
            for (HttpVersion version : values()) {
                if (version.LITERAL.equals(literalVersion)) {
                    return version;
                } else {
                    if (version.MAJOR == major) {
                        if (version.MINOR < minor) {
                            bestVersion = version;
                        }
                    }
                }
            }
        }
        return bestVersion;
    }
}
