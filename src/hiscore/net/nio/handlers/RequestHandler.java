package hiscore.net.nio.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public final class RequestHandler extends DataHandler {

    public RequestHandler(SelectionKey key) {
        super(key);
    }

    @Override
    public void run() {
        ByteBuffer buffer = buffer();
        while (true) {
            int bytesRead;
            try {
                bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    closeChannel();
                    return;
                } else if (bytesRead == 0) {
                    interestOps(SelectionKey.OP_READ);
                    return;
                } else if (bytesRead > 0) {
                    boolean stopReading = buffer.hasRemaining();
                    buffer.flip();
                    interestOps(context.processRequest(buffer) ? SelectionKey.OP_WRITE : SelectionKey.OP_READ);
                    if (stopReading) {
                        return;
                    }
                }
            } catch (IOException swallowed) {
                closeChannel();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                closeChannel();
                return;
            } finally {
                buffer.clear();
            }
        }
    }
}