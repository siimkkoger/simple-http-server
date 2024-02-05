import org.example.httpserver.http.HttpMethod;
import org.example.httpserver.http.HttpParser;
import org.example.httpserver.http.HttpParsingException;
import org.example.httpserver.http.HttpRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void setUp() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() throws IOException, HttpParsingException {
        HttpRequest httpRequest = httpParser.parseHttpRequest(generateValidTestCase());

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    void parseHttpRequestInvalid() {
        assertThatThrownBy(() -> httpParser.parseHttpRequest(generateInvalidTestCase_wrongName()))
                .isInstanceOf(HttpParsingException.class);

        assertThatThrownBy(() -> httpParser.parseHttpRequest(generateInvalidTestCase_nameTooLong()))
                .isInstanceOf(HttpParsingException.class);

        assertThatThrownBy(() -> httpParser.parseHttpRequest(generateInvalidTestCase_tooManySpaces1()))
                .isInstanceOf(HttpParsingException.class);

        assertThatThrownBy(() -> httpParser.parseHttpRequest(generateInvalidTestCase_tooManySpaces2()))
                .isInstanceOf(HttpParsingException.class);

        assertThatThrownBy(() -> httpParser.parseHttpRequest(generateInvalidTestCase_emptyRequestLine()))
                .isInstanceOf(HttpParsingException.class);
    }

    private InputStream generateValidTestCase() {
        String rawDate = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Brave\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8\r\n" +
                "Sec-GPC: 1\r\n" +
                "Accept-Language: en-US,en\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Cookie: mp_628b13b57f789d481577b13542b60034_mixpanel=%7B%22distinct_id%22%3A%20%22%24device%3A18d78cee12a25d-00b8c7cdbbacd1-26001851-232800-18d78cee12a25d%22%2C%22%24device_id%22%3A%20%2218d78cee12a25d-00b8c7cdbbacd1-26001851-232800-18d78cee12a25d%22%2C%22%24initial_referrer%22%3A%20%22%24direct%22%2C%22%24initial_referring_domain%22%3A%20%22%24direct%22%7D\r\n" +
                "\r\n";

        return new ByteArrayInputStream(rawDate.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream generateInvalidTestCase_wrongName() {
        String rawDate = "Get / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Brave\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "\r\n";

        return new ByteArrayInputStream(rawDate.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream generateInvalidTestCase_nameTooLong() {
        String rawDate = "GETTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Brave\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "\r\n";
        return new ByteArrayInputStream(rawDate.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream generateInvalidTestCase_tooManySpaces1() {
        String rawDate = "GET  / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Brave\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "\r\n";
        return new ByteArrayInputStream(rawDate.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream generateInvalidTestCase_tooManySpaces2() {
        String rawDate = "GET / AAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Brave\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "\r\n";
        return new ByteArrayInputStream(rawDate.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream generateInvalidTestCase_emptyRequestLine() {
        String rawDate = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Brave\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "\r\n";
        return new ByteArrayInputStream(rawDate.getBytes(StandardCharsets.UTF_8));
    }
}