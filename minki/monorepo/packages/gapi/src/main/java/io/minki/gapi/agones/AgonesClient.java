package io.minki.gapi.agones;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgonesClient {
    private final String baseUrl;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public AgonesClient() {
        this.baseUrl = String.join("", "http://localhost:", System.getenv("AGONES_SDK_HTTP_PORT"));
    }

    private URI getPathURI(String path) throws URISyntaxException {
        return new URI(String.join("", baseUrl, path));
    }

    public void startHealthTask(int period, TimeUnit unit) {
        new Thread(() -> executorService.scheduleAtFixedRate(this::health, 1, period, unit)).start();
    }

    public void ready() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/ready"))
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void health() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/health"))
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void allocate() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/allocate"))
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/shutdown"))
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addLabel(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/metadata/label"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(String.format("{\"key\": \"%s\", \"value\": \"%s\"}", key, value)))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void playerConnect(UUID playerUuid) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/v1alpha1/lists/players:addValue"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"value\": \"%s\"}", playerUuid.toString())))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void playerDisconnect(UUID playerUuid) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getPathURI("/v1alpha1/lists/players:removeValue"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"value\": \"%s\"}", playerUuid.toString())))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
