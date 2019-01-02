package hiscore.net.nio.handlers;

import hiscore.net.nio.NioHttpServer;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class AcceptHandler extends OperationHandler {

    public AcceptHandler(SelectionKey key) {
        super(key);
    }

    @Override
    public void run() {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        NioHttpServer nioServer = (NioHttpServer) key.attachment();
        try {
            SocketChannel clientChannel;
            while ((clientChannel = serverChannel.accept()) != null) {
                try {
                    nioServer.registerChannel(clientChannel);
                } catch (ClosedChannelException ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            interestOps(SelectionKey.OP_ACCEPT);
        }
    }
}