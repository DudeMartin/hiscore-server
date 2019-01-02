package hiscore.net.nio.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public final class ResponseHandler extends DataHandler {

    public ResponseHandler(SelectionKey key) {
        super(key);
    }

    @Override
    public void run() {
        ByteBuffer writeBuffer = context.responseBuffer();
        try {
            channel.write(writeBuffer);
        } catch (IOException swallowed) {
            closeChannel();
        }
        if (writeBuffer.hasRemaining()) {
            writeBuffer.compact();
            interestOps(SelectionKey.OP_WRITE);
        } else {
            closeChannel();
        }
    }
}