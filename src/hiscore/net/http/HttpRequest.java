package hiscore.net.http;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HttpRequest {

    public final String method;
    public final String requestUri;
    public final List<Header> headers;
    public final String body;

    private HttpRequest(String method, String requestUri, List<Header> headers, String body) {
        this.method = method;
        this.requestUri = requestUri;
        this.headers = Collections.unmodifiableList(headers);
        this.body = body;
    }

    public String queryString() {
        int questionIndex = requestUri.indexOf('?');
        return questionIndex == -1 ? "" : requestUri.substring(questionIndex + 1);
    }

    public static HttpRequest parse(byte[] requestBytes) throws IncompleteRequestException, InvalidRequestException {
        String requestText = new String(requestBytes, StandardCharsets.ISO_8859_1);
        String[] lines = requestText.split("\r\n");
        String requestLine = lines[0];
        String[] requestLineParts = requestLine.split(" ");
        if (requestLineParts.length != 3) {
            throw new IncompleteRequestException();
        }
        String method = requestLineParts[0];
        String requestUri = requestLineParts[1];
        String httpVersion = requestLineParts[2];
        if (!"GET".equals(method) && !"POST".equals(method)
                || !"HTTP/1.0".equals(httpVersion) && !"HTTP/1.1".equals(httpVersion)) {
            throw new InvalidRequestException();
        }
        List<Header> headers = new ArrayList<>();
        for (int lineIndex = 1, lineCount = lines.length; lineIndex < lineCount; lineIndex++) {
            String line = lines[lineIndex];
            if (line.isEmpty()) {
                break;
            }
            int colonIndex = line.lastIndexOf(':');
            if (colonIndex == -1) {
                throw new IncompleteRequestException();
            }
            headers.add(new Header(line.substring(0, colonIndex), line.substring(colonIndex + 1)));
        }
        int blankIndex = requestText.indexOf("\r\n\r\n");
        return new HttpRequest(method, requestUri, headers, blankIndex == -1 ? "" : requestText.substring(blankIndex + 4));
    }
}