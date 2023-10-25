import com.sun.net.httpserver.HttpServer;
import handler.IndexPageHandler;
import handler.IpRangeHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ProxyServer {

    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext("/", new IndexPageHandler());
        server.createContext("/handle", new IpRangeHandler());

        System.out.println("Proxy server start SUCCESS (Port: " + SERVER_PORT + ")");
        server.start();
    }
}
