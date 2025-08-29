package com.aisupport.util;

import com.aisupport.config.AIConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;

import java.util.concurrent.TimeUnit;

/**
 * Utility class to test connections to VLLM servers
 * VLLM uses OpenAI-compatible API interface
 */
public class VllmConnectionTester {
    
    public static void main(String[] args) {
        AIConfig config = new AIConfig();
        testVllmConnection(config);
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
        testBasicConnectivity(client, config);
        
        // Test 2: OpenAI-compatible API endpoint
        testOpenAICompatibility(client, config);
        
        // Test 3: Model availability
        testModelAvailability(client, config);
        
        // Test 4: Simple chat completion
        testChatCompletion(client, config);
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
    
    private static void testOpenAICompatibility(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 2: OpenAI API Compatibility ---");
        try {
            String url = config.getApiUrl() + "v1/models";
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .get();
            
            // Add authentication if available
            if (config.getApiKey() != null && !config.getApiKey().trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + config.getApiKey());
            }
            
            Request request = requestBuilder.build();
            
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("✓ OpenAI-compatible API endpoint is accessible");
                    System.out.println("  Status: " + response.code());
                    String body = response.body() != null ? response.body().string() : "";
                    if (body.contains("data") && body.contains("id")) {
                        System.out.println("  Response contains model list");
                        System.out.println("  This confirms OpenAI API compatibility");
                    }
                } else {
                    System.out.println("✗ OpenAI API endpoint returned error: " + response.code());
                    if (response.code() == 403) {
                        System.out.println("  This suggests authentication or authorization issues");
                    } else if (response.code() == 404) {
                        System.out.println("  Endpoint not found - check if VLLM is running with OpenAI compatibility");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("✗ OpenAI API compatibility test failed: " + e.getMessage());
        }
    }
    
    private static void testModelAvailability(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 3: Model Availability ---");
        try {
            String url = config.getApiUrl() + "v1/models/" + config.getModelName();
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .get();
            
            // Add authentication if available
            if (config.getApiKey() != null && !config.getApiKey().trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + config.getApiKey());
            }
            
            Request request = requestBuilder.build();
            
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("✓ Model '" + config.getModelName() + "' is available");
                    System.out.println("  Status: " + response.code());
                    String body = response.body() != null ? response.body().string() : "";
                    if (body.contains("id") && body.contains("object")) {
                        System.out.println("  Model details retrieved successfully");
                    }
                } else {
                    System.out.println("✗ Model '" + config.getModelName() + "' test failed: " + response.code());
                    if (response.code() == 404) {
                        System.out.println("  Model not found - check if it's loaded on the VLLM server");
                    } else if (response.code() == 403) {
                        System.out.println("  Access forbidden - check authentication and permissions");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Model availability test failed: " + e.getMessage());
        }
    }
    
    private static void testChatCompletion(OkHttpClient client, AIConfig config) {
        System.out.println("\n--- Test 4: Chat Completion Test ---");
        try {
            String url = config.getApiUrl() + "v1/chat/completions";
            
            // Simple test message
            String requestBody = "{\n" +
                    "  \"model\": \"" + config.getModelName() + "\",\n" +
                    "  \"messages\": [\n" +
                    "    {\n" +
                    "      \"role\": \"user\",\n" +
                    "      \"content\": \"Hello, this is a connection test. Please respond with 'Connection successful!'\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"max_tokens\": 50,\n" +
                    "  \"temperature\": " + config.getTemperature() + "\n" +
                    "}";
            
            RequestBody body = RequestBody.create(
                requestBody, 
                MediaType.parse("application/json")
            );
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json");
            
            // Add authentication if available
            if (config.getApiKey() != null && !config.getApiKey().trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + config.getApiKey());
            }
            
            Request request = requestBuilder.build();
            
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("✓ Chat completion test successful");
                    System.out.println("  Status: " + response.code());
                    String responseBody = response.body() != null ? response.body().string() : "";
                    if (responseBody.contains("choices") && responseBody.contains("message")) {
                        System.out.println("  Response format is correct");
                        System.out.println("  VLLM is fully operational!");
                    }
                } else {
                    System.out.println("✗ Chat completion test failed: " + response.code());
                    if (response.code() == 400) {
                        System.out.println("  Bad request - check model name and parameters");
                    } else if (response.code() == 503) {
                        System.out.println("  Service unavailable - VLLM might be overloaded or model not ready");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Chat completion test failed: " + e.getMessage());
        }
    }
}

