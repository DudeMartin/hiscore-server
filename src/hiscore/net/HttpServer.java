package hiscore.net;

import java.io.Closeable;
import java.io.IOException;

public interface HttpServer extends Closeable {

    void start(int port) throws IOException;
}