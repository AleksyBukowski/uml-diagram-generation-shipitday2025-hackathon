package com.aisupport.util;

import com.aisupport.config.AIConfig;
import com.aisupport.config.CustomHttpClient;
import com.aisupport.config.CustomHttpClientBuilder;
import com.aisupport.service.VllmService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Utility class to test connections to AI servers
 */
public class ConnectionTester {

    public static void main(String[] args) {
        AIConfig config = new AIConfig();

        if ("vllm".equalsIgnoreCase(config.getEngineType())) {
            testVllmConnection(config);
        } else {
            testOllamaConnection(config);
        }
    }

    public static void testOllamaConnection(AIConfig config) {
        System.out.println("=== Testing Ollama Connection ===");
        System.out.println("API URL: " + config.getApiUrl());
        System.out.println("Model: " + config.getModelName());
        System.out.println("API Key: " + (config.getApiKey() != null ? "***" + config.getApiKey().substring(Math.max(0, config.getApiKey().length() - 4)) : "None"));

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // Test 1: Basic connectivity
        testBasicConnectivity(client, config);

        // Test 2: API endpoint
        testAPIEndpoint(client, config);

        // Test 3: Model availability
        testModelAvailability(client, config);
    }

    private static void testBasicConnectivity(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 1: Basic Connectivity ---");
        try {
            String baseUrl = config.getApiUrl();
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            Request request = new Request.Builder()
                    .url(baseUrl)
                    .get()
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                System.out.println("✓ Server is reachable");
                System.out.println("  Status: " + response.code());
                System.out.println("  Server: " + response.header("Server", "Unknown"));
            }
        } catch (Exception e) {
            System.out.println("✗ Server is not reachable: " + e.getMessage());
        }
    }

    private static void testAPIEndpoint(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 2: API Endpoint ---");
        try {
            String url = config.getApiUrl() + "api/tags";

            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .get();

            // Add authentication if available
            if (config.getApiKey() != null && !config.getApiKey().trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + config.getApiKey());
                requestBuilder.addHeader("X-API-Key", config.getApiKey());
            }

            Request request = requestBuilder.build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("✓ API endpoint is accessible");
                    System.out.println("  Status: " + response.code());
                    String body = response.body() != null ? response.body().string() : "";
                    if (body.contains("models")) {
                        System.out.println("  Response contains model information");
                    }
                } else {
                    System.out.println("✗ API endpoint returned error: " + response.code());
                    if (response.code() == 403) {
                        System.out.println("  This suggests authentication or authorization issues");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("✗ API endpoint test failed: " + e.getMessage());
        }
    }

    private static void testModelAvailability(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 3: Model Availability ---");
        try {
            String url = config.getApiUrl() + "api/show";

            String requestBody = "{\"name\":\"" + config.getModelName() + "\"}";

            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(okhttp3.RequestBody.create(
                            requestBody,
                            okhttp3.MediaType.parse("application/json")
                    ));

            // Add authentication if available
            if (config.getApiKey() != null && !config.getApiKey().trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + config.getApiKey());
                requestBuilder.addHeader("X-API-Key", config.getApiKey());
            }

            requestBuilder.addHeader("Content-Type", "application/json");

            Request request = requestBuilder.build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("✓ Model '" + config.getModelName() + "' is available");
                    System.out.println("  Status: " + response.code());
                } else {
                    System.out.println("✗ Model '" + config.getModelName() + "' test failed: " + response.code());
                    if (response.code() == 404) {
                        System.out.println("  Model not found - check if it's installed on the server");
                    } else if (response.code() == 403) {
                        System.out.println("  Access forbidden - check authentication and permissions");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Model availability test failed: " + e.getMessage());
        }
    }

    public static void testVllmConnection(AIConfig config) {
        System.out.println("=== Testing VLLM Connection ===");
        System.out.println("API URL: " + config.getApiUrl());
        System.out.println("Model: " + config.getModelName());
        System.out.println("API Key: " + (config.getApiKey() != null ? "***" + config.getApiKey().substring(Math.max(0, config.getApiKey().length() - 4)) : "None"));

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // Test 1: Basic connectivity
        testVllmBasicConnectivity(client, config);

        // Test 2: Model availability
        testVllmModelAvailability(config);

    }

    private static void testVllmBasicConnectivity(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 1: Basic Connectivity ---");
        try {
            String baseUrl = config.getApiUrl();
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            Request request = new Request.Builder()
                    .url(baseUrl)
                    .get()
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                System.out.println("✓ Server is reachable");
                System.out.println("  Status: " + response.code());
                System.out.println("  Server: " + response.header("Server", "Unknown"));
            }
        } catch (Exception e) {
            System.out.println("✗ Server is not reachable: " + e.getMessage());
        }
    }



    private static void testVllmModelAvailability(AIConfig config) {
        HttpClient.Builder client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1);
        CustomHttpClientBuilder customHttpClientBuilder = CustomHttpClient.builder().httpClientBuilder(client);
        OpenAiChatModel modelChat = OpenAiChatModel.builder()
                .httpClientBuilder(customHttpClientBuilder)
                .baseUrl(config.getApiUrl())
                .customHeaders(Map.of("Authorization", "Bearer " + config.getApiKey()))
                .modelName(config.getModelName())
                .temperature(config.getTemperature())
                .timeout(Duration.of(5, ChronoUnit.MINUTES))
                .build();

        System.out.println("\n--- Test 2: Model Availability ---");
        try {
            String url = config.getApiUrl();
            ChatMessage message = UserMessage.from("Are You working?");
            ChatRequest newRequest = ChatRequest.builder()
                    .messages(message)
                    .build();

                ChatResponse response = modelChat.chat(newRequest);
            String body = response.aiMessage() != null ? response.aiMessage().text() : "";
                System.out.println("  Response details retrieved successfully");
            System.out.println("✓ Model '" + config.getModelName() + "' is available");
            System.out.println("  Status: OK");

        } catch (Exception e) {

                System.out.println("✗ Model '" + config.getModelName() + "' test failed: ");
                if (e.getCause() != null && e.getCause().getMessage().contains("404")) {
                    System.out.println("  Model not found - check if it's loaded on the VLLM server");
                } else if (e.getCause() != null && e.getCause().getMessage().contains("403")) {
                    System.out.println("  Access forbidden - check authentication and permissions");
                }

            System.out.println("✗ Model availability test failed: " + e.getMessage());
        }
    }
}
