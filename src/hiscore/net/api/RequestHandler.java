package hiscore.net.api;

import hiscore.net.http.HttpRequest;
import hiscore.net.http.HttpResponseBuilder;
import hiscore.official.HiscoreType;
import hiscore.storage.RecordAccessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

abstract class RequestHandler {

    final HttpRequest request;
    final Map<String, List<String>> requestParameters;
    final RecordAccessor recordAccessor;

    RequestHandler(HttpRequest request, RecordAccessor recordAccessor) {
        this.request = request;
        this.requestParameters = Collections.unmodifiableMap(HttpRequest.parseParameters(
                "GET".equals(request.method) ? request.queryString() : request.body));
        this.recordAccessor = recordAccessor;
    }

    public final HttpResponseBuilder generateResponse() throws IOException {
        Optional<String> nameOptional = parameterValue("name");
        if (!nameOptional.isPresent()) {
            return badRequestResponse();
        }
        Optional<HiscoreType> typeOptional = parameterValue("type", type -> {
            try {
                HiscoreType.valueOf(type.trim().toUpperCase());
                return true;
            } catch (IllegalArgumentException expected) {
                return false;
            }
        }).map(type -> HiscoreType.valueOf(type.trim().toUpperCase()));
        if (!typeOptional.isPresent()) {
            return badRequestResponse();
        }
        return generateResponseInternal(nameOptional.get(), typeOptional.get());
    }

    abstract HttpResponseBuilder generateResponseInternal(String name, HiscoreType type) throws IOException;

    final Optional<String> parameterValue(String name, Predicate<String> validationFunction) {
        List<String> parameterValues = requestParameters.get(name);
        return parameterValues == null ? Optional.empty() : parameterValues.stream().filter(validationFunction).findFirst();
    }

    final Optional<String> parameterValue(String name) {
        return parameterValue(name, value -> true);
    }

    final HttpResponseBuilder badRequestResponse() {
        return new HttpResponseBuilder("400 Bad Request");
    }
}