package hiscore;

import hiscore.net.HttpServer;
import hiscore.net.nio.NioHttpServer;
import hiscore.storage.RecordAccessor;
import hiscore.storage.file.FileRecordAccessor;

import java.io.IOException;
import java.nio.file.Paths;

public class Application {

    public static void main(String[] arguments) throws IOException {
        int port = 80;
        String storagePath = System.getProperty("user.dir");
        if (arguments.length > 0) {
            port = Integer.parseInt(arguments[0]);
            if (arguments.length > 1) {
                storagePath = arguments[1];
            }
        }
        System.out.println("Starting HTTP server on port " + port + "...");
        RecordAccessor recordAccessor = new FileRecordAccessor(Paths.get(storagePath));
        HttpServer httpServer = new NioHttpServer(recordAccessor);
        httpServer.start(port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            try {
                httpServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}