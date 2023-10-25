package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.net.util.SubnetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IpRangeHandler implements HttpHandler {

    private static final int SSL_PORT = 443;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());

        Gson gson = new Gson();
        RequestBody requestBody = gson.fromJson(reader, RequestBody.class);

        String[] ips = getAllIpsFromRange(requestBody.getIpRange());

        int ipsPerThread = ips.length / requestBody.getThreadsAmount();
        ExecutorService executor = Executors.newFixedThreadPool(requestBody.getThreadsAmount());
        for (int i = 0; i < requestBody.getThreadsAmount(); i++) {
            int startIndex = i * ipsPerThread;
            int endIndex = (i == requestBody.getThreadsAmount() - 1) ? ips.length : ipsPerThread * (i + 1);
            String[] ipsToScanByExecutor = java.util.Arrays.copyOfRange(ips, startIndex, endIndex);
            executor.execute(() -> {
                for (String ip : ipsToScanByExecutor) {
                    try {
                        SSLContext context = SSLContext.getDefault();
                        SSLSocketFactory factory = context.getSocketFactory();
                        SSLSocket socket = (SSLSocket) factory.createSocket(ip, SSL_PORT);
                        socket.startHandshake();
                        Certificate[] certificates = socket.getHandshakeSession().getPeerCertificates();
                        for (Certificate cert : certificates) {
                            saveDomainsFromCertificate(cert);
                        }
                        socket.close();
                        } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    } catch (SSLPeerUnverifiedException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
    }

    private static void saveDomainsFromCertificate(java.security.cert.Certificate certificate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("domains.txt", true))) {
            Document document = Jsoup.parse(certificate.toString());
            String domain = document.select("domain").text();
            System.out.println(domain);
            writer.write(domain + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] getAllIpsFromRange(String ipRange) {
        SubnetUtils subnetUtils = new SubnetUtils(ipRange);
        return subnetUtils.getInfo().getAllAddresses();
    }
}
