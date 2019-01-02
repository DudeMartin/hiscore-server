package hiscore.net.nio;

import hiscore.net.HttpServer;
import hiscore.net.nio.handlers.AcceptHandler;
import hiscore.net.nio.handlers.RequestHandler;
import hiscore.net.nio.handlers.ResponseHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public final class NioHttpServer extends Thread implements HttpServer {

    private final ReentrantLock registerLock = new ReentrantLock();
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private ExecutorService workerPool;
    private volatile boolean running;

    @Override
    public synchronized void start(int port) throws IOException {
        if (workerPool != null) {
            throw new IllegalStateException("The server has already been started.");
        }
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT, this);
        workerPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        running = true;
        start();
    }

    @Override
    public synchronized void close() throws IOException {
        if (running) {
            running = false;
            selector.wakeup();
            try {
                join();
            } catch (InterruptedException ignored) {}
            workerPool.shutdown();
            try {
                selector.close();
            } finally {
                serverChannel.close();
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            registerLock.lock();
            registerLock.unlock();
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    workerPool.submit(new AcceptHandler(key));
                } else if (key.isReadable()) {
                    workerPool.submit(new RequestHandler(key));
                } else if (key.isWritable()) {
                    workerPool.submit(new ResponseHandler(key));
                }
            }
        }
    }

    public void registerChannel(SocketChannel clientChannel) throws ClosedChannelException {
        try {
            clientChannel.configureBlocking(false);
        } catch (IOException swallowed) {
            try {
                clientChannel.close();
            } catch (IOException ignored) {}
            throw new ClosedChannelException();
        }
        registerLock.lock();
        try {
            selector.wakeup();
            clientChannel.register(selector, SelectionKey.OP_READ, new RequestContext());
        } finally {
            registerLock.unlock();
        }
    }
}