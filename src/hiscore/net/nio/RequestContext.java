package hiscore.net.nio;

import hiscore.net.http.HttpResponseBuilder;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public final class RequestContext {

    private static final ThreadLocal<byte[]> threadTransferBuffer = ThreadLocal.withInitial(() -> new byte[1024]);
    private final ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
    private ByteBuffer responseBuffer;

    RequestContext() {

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

    private boolean processAccumulatedRequest() {
        responseBuffer = ByteBuffer.wrap(new HttpResponseBuilder()
                .withStatus("200 OK")
                .withBody("Hello, world!").buildByteArray());
        return true;
    }
}