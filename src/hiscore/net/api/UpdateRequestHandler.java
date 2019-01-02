package hiscore.net.api;

import hiscore.net.http.HttpRequest;
import hiscore.net.http.HttpResponseBuilder;
import hiscore.official.HiscoreType;
import hiscore.storage.RecordAccessor;

import java.io.IOException;

public final class UpdateRequestHandler extends RequestHandler {

    public UpdateRequestHandler(HttpRequest request, RecordAccessor recordAccessor) {
        super(request, recordAccessor);
    }

    @Override
    HttpResponseBuilder generateResponseInternal(String name, HiscoreType type) throws IOException {
        recordAccessor.updateRecord(name, type);
        return new HttpResponseBuilder("200 OK");
    }
}