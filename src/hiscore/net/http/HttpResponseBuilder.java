package hiscore.net.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpResponseBuilder {

    private static final Charset RESPONSE_CHARSET = StandardCharsets.ISO_8859_1;
    private String status;
    private String body;

    public HttpResponseBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public HttpResponseBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ");
        builder.append(status);
        builder.append('\n');
        builder.append("Date: ");
        builder.append(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        builder.append('\n');
        builder.append("Content-Type: text/html; charset=");
        builder.append(RESPONSE_CHARSET.name());
        builder.append('\n');
        builder.append("Content-Length: ");
        builder.append(body.getBytes(RESPONSE_CHARSET).length);
        builder.append('\n');
        builder.append("Connection: closed");
        builder.append("\r\n\n");
        builder.append(body);
        return builder.toString();
    }

    public byte[] buildByteArray() {
        return build().getBytes(RESPONSE_CHARSET);
    }
}