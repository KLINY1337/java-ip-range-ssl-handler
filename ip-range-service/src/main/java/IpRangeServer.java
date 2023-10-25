import com.sun.net.httpserver.HttpServer;
import handler.IpRangeHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class IpRangeServer {

    private static final int SERVER_PORT = 8081;
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext("/", new IpRangeHandler());

        System.out.println("Ip range handler server start SUCCESS (Port: " + SERVER_PORT + ")");
        server.start();
    }
}
