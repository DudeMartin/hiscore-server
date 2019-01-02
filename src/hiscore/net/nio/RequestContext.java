package hiscore.net.nio;

import hiscore.net.api.LookupRequestHandler;
import hiscore.net.api.UpdateRequestHandler;
import hiscore.net.http.HttpRequest;
import hiscore.net.http.HttpResponseBuilder;
import hiscore.net.http.IncompleteRequestException;
import hiscore.net.http.InvalidRequestException;
import hiscore.official.HiscoreException;
import hiscore.storage.RecordAccessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class RequestContext {

    private static final ThreadLocal<byte[]> threadTransferBuffer = ThreadLocal.withInitial(() -> new byte[1024]);
    private final ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
    private final RecordAccessor recordAccessor;
    private ByteBuffer responseBuffer;

    RequestContext(RecordAccessor recordAccessor) {
        this.recordAccessor = recordAccessor;
    }

    public boolean processRequest(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            if (buffer.hasArray()) {
                int position = buffer.position();
                int remaining = buffer.remaining();
                accumulator.write(buffer.array(), buffer.arrayOffset() + position, remaining);
                buffer.position(position + remaining);
            } else {
                byte[] transferBuffer = threadTransferBuffer.get();
                int transferAmount = Math.min(transferBuffer.length, buffer.remaining());
                buffer.get(transferBuffer, 0, transferAmount);
                accumulator.write(transferBuffer, 0, transferAmount);
            }
        }
        return processAccumulatedRequest();
    }

    public ByteBuffer responseBuffer() {
        return responseBuffer.asReadOnlyBuffer();
    }

    private void sendResponse(HttpResponseBuilder builder) {
        responseBuffer = ByteBuffer.wrap(builder.build());
    }

    private boolean processAccumulatedRequest() {
        HttpRequest request;
        try {
            request = HttpRequest.parse(accumulator.toByteArray());
        } catch (IncompleteRequestException e) {
            return false;
        } catch (InvalidRequestException e) {
            sendResponse(new HttpResponseBuilder("400 Bad Request"));
            return true;
        }
        HttpResponseBuilder response;
        try {
            if ("GET".equals(request.method)) {
                response = new LookupRequestHandler(request, recordAccessor).generateResponse();
            } else {
                response = new UpdateRequestHandler(request, recordAccessor).generateResponse();
            }
        } catch (HiscoreException e) {
            response = new HttpResponseBuilder("204 No Content");
        } catch (IOException e) {
            e.printStackTrace();
            response = new HttpResponseBuilder("500 Internal Server Error");
        }
        sendResponse(response);
        return true;
    }
}