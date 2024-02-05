package org.example.httpserver.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class HttpVersionTest {

    @Test
    void getBestCompatibleVersion() {
        HttpVersion httpVersion = null;
        try {
            httpVersion = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (HttpParsingException e) {
            fail();
        }

        assertThat(httpVersion).isNotNull();
        assertThat(httpVersion).isEqualTo(HttpVersion.HTTP_1_1);
    }

    @Test
    void getBestCompatibleVersion_higherVersion() {
        HttpVersion httpVersion = null;
        try {
            httpVersion = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
        } catch (HttpParsingException e) {
            fail();
        }

        assertThat(httpVersion).isNotNull();
        assertThat(httpVersion).isEqualTo(HttpVersion.HTTP_1_1);
    }

}