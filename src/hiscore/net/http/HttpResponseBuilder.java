package hiscore.net.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

public final class HttpResponseBuilder {

    public static final Charset RESPONSE_CHARSET = StandardCharsets.ISO_8859_1;
    private String statusLine;
    private String contentType;
    private byte[] body = new byte[0];

    public HttpResponseBuilder(String statusLine) {
        this.statusLine = validateNonBlank(statusLine, "The status line cannot be blank.");
        withMediaType("text/html");
    }

    public HttpResponseBuilder withMediaType(String mediaType) {
        mediaType = validateNonBlank(mediaType, "The media type cannot be blank.");
        if (mediaType.startsWith("text/")) {
            this.contentType = mediaType + "; charset=" + RESPONSE_CHARSET.name();
        } else {
            this.contentType = mediaType;
        }
        return this;
    }

    public HttpResponseBuilder withBody(byte[] body) {
        this.body = Objects.requireNonNull(body);
        return this;
    }

    public HttpResponseBuilder withBody(String body) {
        return withBody(body.getBytes(RESPONSE_CHARSET));
    }

    public byte[] build() {
        int bodyLength = body.length;
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ");
        builder.append(statusLine);
        builder.append("\r\n");
        builder.append("Date: ");
        builder.append(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        builder.append("\r\n");
        builder.append("Content-Type: ");
        builder.append(contentType);
        builder.append("\r\n");
        builder.append("Content-Length: ");
        builder.append(bodyLength);
        builder.append("\r\n");
        builder.append("Connection: close");
        builder.append("\r\n\r\n");
        byte[] headerBytes = builder.toString().getBytes(RESPONSE_CHARSET);
        byte[] responseBytes = Arrays.copyOf(headerBytes, headerBytes.length + bodyLength);
        System.arraycopy(body, 0, responseBytes, headerBytes.length, bodyLength);
        return responseBytes;
    }

    private static String validateNonBlank(String string, String message) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return string;
    }
}