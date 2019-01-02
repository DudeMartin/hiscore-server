package hiscore.net.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return questionIndex == -1 ? "" : requestUri.substring(questionIndex + 1).toLowerCase();
    }

    public static HttpRequest parse(byte[] requestBytes) throws IncompleteRequestException, InvalidRequestException {
        String requestText = new String(requestBytes, StandardCharsets.ISO_8859_1);
        String[] lines = requestText.split("\r\n");
        String requestLine = lines[0];
        String[] requestLineParts = requestLine.split(" ");
        if (requestLineParts.length < 3) {
            throw new IncompleteRequestException();
        } else if (requestLineParts.length > 3) {
            throw new InvalidRequestException();
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
        if (blankIndex == -1) {
            throw new IncompleteRequestException();
        }
        return new HttpRequest(method, requestUri, headers, requestText.substring(blankIndex + 4));
    }

    public static Map<String, List<String>> parseParameters(String queryString) {
        Map<String, List<String>> queryParameters = new HashMap<>();
        String[] parameters = queryString.split("&");
        for (String parameter : parameters) {
            int equalsIndex = parameter.indexOf('=');
            if (equalsIndex != -1) {
                String name = parameter.substring(0, equalsIndex);
                String value = parameter.substring(equalsIndex + 1);
                try {
                    queryParameters.merge(name, Collections.singletonList(URLDecoder.decode(value, StandardCharsets.ISO_8859_1.name())), (oldValues, newValues) ->
                            Stream.concat(oldValues.stream(), newValues.stream()).collect(Collectors.toList()));
                } catch (UnsupportedEncodingException impossible) {}
            }
        }
        return queryParameters;
    }
}