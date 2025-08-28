package com.aisupport.config;

import dev.langchain4j.http.client.HttpClientBuilder;

import java.net.http.HttpClient;
import java.time.Duration;

public class CustomHttpClientBuilder implements HttpClientBuilder {
    private HttpClient.Builder httpClientBuilder;
    private Duration connectTimeout;
    private Duration readTimeout;

    public CustomHttpClientBuilder() {
    }

    public HttpClient.Builder httpClientBuilder() {
        return this.httpClientBuilder;
    }

    public CustomHttpClientBuilder httpClientBuilder(HttpClient.Builder httpClientBuilder) {
        this.httpClientBuilder = httpClientBuilder;
        return this;
    }

    public Duration connectTimeout() {
        return this.connectTimeout;
    }

    public CustomHttpClientBuilder connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Duration readTimeout() {
        return this.readTimeout;
    }

    public CustomHttpClientBuilder readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public CustomHttpClient build() {
        return new CustomHttpClient(this);
    }
}
