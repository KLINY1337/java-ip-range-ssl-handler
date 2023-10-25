import io.javalin.Javalin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

public class ViewProvider {

    private static final int SERVER_PORT = 8082;

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(SERVER_PORT);
        System.out.println("View server start SUCCESS (Port: " + SERVER_PORT + ")");
        app.get("/", context -> context.html(readFileFromResourcesToString("index.html")));

    }

    private static String readFileFromResourcesToString(String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            return new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
}
