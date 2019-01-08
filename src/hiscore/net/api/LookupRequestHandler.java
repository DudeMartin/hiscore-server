package hiscore.net.api;

import hiscore.model.PlayerListing;
import hiscore.net.http.HttpRequest;
import hiscore.net.http.HttpResponseBuilder;
import hiscore.official.HiscoreType;
import hiscore.storage.PlayerRecord;
import hiscore.storage.RecordAccessor;
import hiscore.storage.file.util.PlayerListingCodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class LookupRequestHandler extends PlayerRequestHandler {

    public LookupRequestHandler(HttpRequest request, RecordAccessor recordAccessor) {
        super(request, recordAccessor);
    }

    @Override
    HttpResponseBuilder generateResponseInternal(String name, HiscoreType type) throws IOException {
        String startTime;
        Optional<String> endTimeOptional;
        try {
            Optional<String> startTimeOptional = parameterValue("start", time -> Long.parseLong(time) >= 0);
            if (!startTimeOptional.isPresent()) {
                return badRequestResponse();
            }
            startTime = startTimeOptional.get();
            endTimeOptional = parameterValue("end", time -> Long.parseLong(time) > Long.parseLong(startTime));
            if (!endTimeOptional.isPresent()) {
                return badRequestResponse();
            }
        } catch (NumberFormatException swallowed) {
            return badRequestResponse();
        }
        PlayerRecord record = recordAccessor.getRecord(name, type);
        List<PlayerListing> listingsBetween = record.listingsBetween(
                Instant.ofEpochMilli(Long.parseLong(startTime)),
                Instant.ofEpochMilli(Long.parseLong(endTimeOptional.get())));
        ByteBuffer dataBuffer = ByteBuffer.allocate(listingsBetween.size() * PlayerListingCodec.BYTES_PER_LISTING);
        listingsBetween.stream().map(PlayerListingCodec::toBytes).forEach(dataBuffer::put);
        return new HttpResponseBuilder("200 OK").withMediaType("application/octet-stream").withBody(dataBuffer.array());
    }
}