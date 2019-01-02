package hiscore;

import hiscore.net.HttpServer;
import hiscore.net.nio.NioHttpServer;

import java.io.IOException;

public class Application {

    public static void main(String[] arguments) throws IOException {
        int port = 80;
        if (arguments.length > 0) {
            port = Integer.parseInt(arguments[0]);
        }
        System.out.println("Starting HTTP server on port " + port + "...");
        HttpServer httpServer = new NioHttpServer();
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