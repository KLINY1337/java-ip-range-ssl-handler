package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;

public class IndexPageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8082");

        try (CloseableHttpResponse response = httpClient.execute(request);
             OutputStream os = exchange.getResponseBody()
        ) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                exchange.getResponseHeaders().set("Content-type", "text/html");
                exchange.sendResponseHeaders(200, responseString.length());
                os.write(responseString.getBytes());
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        httpClient.close();
    }
}
