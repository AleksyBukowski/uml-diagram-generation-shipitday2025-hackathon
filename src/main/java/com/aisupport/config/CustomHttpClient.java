package com.aisupport.config;

import dev.langchain4j.exception.HttpException;
import dev.langchain4j.exception.TimeoutException;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventListenerUtils;
import dev.langchain4j.http.client.sse.ServerSentEventParser;
import dev.langchain4j.internal.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;
public class CustomHttpClient implements HttpClient {
    private final java.net.http.HttpClient delegate;
    private final Duration readTimeout;
    private static final Logger log = LogManager.getLogger(CustomHttpClient.class);

    public CustomHttpClient(CustomHttpClientBuilder builder) {
        java.net.http.HttpClient.Builder httpClientBuilder = (java.net.http.HttpClient.Builder) Utils.getOrDefault(builder.httpClientBuilder(), java.net.http.HttpClient::newBuilder);
        if (builder.connectTimeout() != null) {
            httpClientBuilder.connectTimeout(builder.connectTimeout());
        }

        this.delegate = httpClientBuilder.build();
        this.readTimeout = builder.readTimeout();
    }

    public static CustomHttpClientBuilder builder() {
        return new CustomHttpClientBuilder();
    }

    public SuccessfulHttpResponse execute(HttpRequest request) throws HttpException {
        try {
            java.net.http.HttpRequest jdkRequest = this.toJdkRequest(request);
            HttpResponse<String> jdkResponse = this.delegate.send(jdkRequest, HttpResponse.BodyHandlers.ofString());
            if (!isSuccessful(jdkResponse)) {
                throw new HttpException(jdkResponse.statusCode(), (String)jdkResponse.body());
            } else {
                return fromJdkResponse(jdkResponse, (String)jdkResponse.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(HttpRequest request, ServerSentEventParser parser, ServerSentEventListener listener) {
        java.net.http.HttpRequest jdkRequest = this.toJdkRequest(request);
        this.delegate.sendAsync(jdkRequest, HttpResponse.BodyHandlers.ofInputStream()).thenAccept((jdkResponse) -> {
            if (!isSuccessful(jdkResponse)) {
                HttpException exception = new HttpException(jdkResponse.statusCode(), readBody(jdkResponse));
                ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onError(exception));
            } else {
                SuccessfulHttpResponse response = fromJdkResponse(jdkResponse, (String)null);
                ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onOpen(response));

                try {
                    try (InputStream inputStream = (InputStream)jdkResponse.body()) {
                        parser.parse(inputStream, listener);
                        Objects.requireNonNull(listener);
                        ServerSentEventListenerUtils.ignoringExceptions(listener::onClose);
                    }

                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }).exceptionally((throwable) -> {
            if (throwable.getCause() instanceof HttpTimeoutException) {
                ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onError(new TimeoutException(throwable)));
            } else {
                ServerSentEventListenerUtils.ignoringExceptions(() -> listener.onError(throwable));
            }

            return null;
        });
    }

    private java.net.http.HttpRequest toJdkRequest(HttpRequest request) {
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder().uri(URI.create(request.url()));
        request.headers().forEach((name, values) -> {
            if (values != null) {
                values.forEach((value) -> builder.header(name, value));
            }

        });
        java.net.http.HttpRequest.BodyPublisher bodyPublisher = request.body() != null ? java.net.http.HttpRequest.BodyPublishers.ofString(request.body()) : java.net.http.HttpRequest.BodyPublishers.noBody();
        builder.method(request.method().name(), bodyPublisher);
        if (this.readTimeout != null) {
            builder.timeout(this.readTimeout);
        }

        return builder.build();
    }

    private static SuccessfulHttpResponse fromJdkResponse(HttpResponse<?> response, String body) {
        return SuccessfulHttpResponse.builder().statusCode(response.statusCode()).headers(response.headers().map()).body(body).build();
    }

    private static boolean isSuccessful(HttpResponse<?> response) {
        int statusCode = response.statusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    private static String readBody(HttpResponse<InputStream> response) {
        try {
            String var3;
            try (
                    InputStream inputStream = (InputStream)response.body();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ) {
                var3 = (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }

            return var3;
        } catch (IOException e) {
            log.error(e);
            return "Cannot read error response body: " + e.getMessage();
        }
    }
}
