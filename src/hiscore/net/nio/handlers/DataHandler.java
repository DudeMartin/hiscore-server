package hiscore.net.nio.handlers;

import hiscore.net.nio.RequestContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

abstract class DataHandler extends OperationHandler {

    private static final ThreadLocal<ByteBuffer> threadBuffer = ThreadLocal.withInitial(() -> ByteBuffer.allocate(1024));
    final SocketChannel channel;
    final RequestContext context;

    DataHandler(SelectionKey key) {
        super(key);
        this.channel = (SocketChannel) key.channel();
        this.context = (RequestContext) key.attachment();
    }

    final ByteBuffer buffer() {
        return threadBuffer.get();
    }

    final void closeChannel() {
        key.cancel();
        try {
            channel.close();
        } catch (IOException ignored) {}
    }
}