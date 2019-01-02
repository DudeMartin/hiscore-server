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
        if (validateNonBlank(mediaType, "The media type cannot be blank.").startsWith("text/")) {
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

    public byte[] build() {
        int bodyLength = body.length;
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("HTTP/1.1 ");
        headerBuilder.append(statusLine);
        headerBuilder.append("\r\n");
        headerBuilder.append("Date: ");
        headerBuilder.append(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        headerBuilder.append("\r\n");
        headerBuilder.append("Content-Type: ");
        headerBuilder.append(contentType);
        headerBuilder.append("\r\n");
        headerBuilder.append("Content-Length: ");
        headerBuilder.append(bodyLength);
        headerBuilder.append("\r\n");
        headerBuilder.append("Connection: close");
        headerBuilder.append("\r\n\r\n");
        byte[] headerBytes = headerBuilder.toString().getBytes(RESPONSE_CHARSET);
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