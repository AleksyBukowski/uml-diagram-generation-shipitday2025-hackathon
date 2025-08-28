package com.aisupport.util;

import com.aisupport.config.AIConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

/**
 * Utility class to test connections to AI servers
 */
public class ConnectionTester {
    
    public static void main(String[] args) {
        AIConfig config = new AIConfig();
        testOllamaConnection(config);
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
}
