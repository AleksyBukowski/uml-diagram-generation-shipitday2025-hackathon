package com.aisupport.service;

import com.aisupport.config.AIBasicConfig;
import dev.langchain4j.model.ollama.OllamaChatModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service implementation for Ollama AI models
 */
public class OllamaService implements AIService {
    
    private final AIBasicConfig config;
    private final OllamaChatModel chatModel;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public OllamaService(AIBasicConfig config) {
        this.config = config;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
        this.chatModel = createChatModel();
    }
    
    private OllamaChatModel createChatModel() {

        return OllamaChatModel.builder()
                .customHeaders(Map.of("Authorization", "Bearer " + config.getApiKey()))
                .baseUrl(config.getApiUrl())
                .modelName(config.getModelName())
                .temperature(config.getTemperature())
                .build();
    }




    
    @Override
    public String sendMessage(String message) {
        try {
            // First try the standard LangChain4j approach
            return chatModel.chat(message);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("403")) {
                // Try custom HTTP request with authentication
                return tryCustomOllamaRequest(message);
            } else if (errorMsg.contains("404")) {
                return "Error: Model not found (404). Please check if the model '" + 
                       config.getModelName() + "' is available on the server.";
            } else if (errorMsg.contains("Connection refused") || errorMsg.contains("timeout")) {
                return "Error: Cannot connect to Ollama server. Please check:\n" +
                       "1. Server is running at: " + config.getApiUrl() + "\n" +
                       "2. Network connectivity\n" +
                       "3. Firewall settings";
            } else {
                return "Error communicating with Ollama: " + errorMsg;
            }
        }
    }

    private String tryCustomOllamaRequest(String message) {
        try {
            String url = config.getApiUrl() + "api/generate";
            String requestBody = String.format(
                "{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}",
                config.getModelName(),
                message.replace("\"", "\\\"")
            );
            
            Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create(
                    requestBody, 
                    okhttp3.MediaType.parse("application/json")
                ));
            
            // Add authentication header if API key is provided
            if (config.getApiKey() != null && !config.getApiKey().trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + config.getApiKey());
                requestBuilder.addHeader("X-API-Key", config.getApiKey());
            }
            
            // Add common headers
            requestBuilder.addHeader("Content-Type", "application/json");
            requestBuilder.addHeader("User-Agent", "AI-Support/1.0");
            
            Request request = requestBuilder.build();
            
            try (okhttp3.Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    return jsonNode.path("response").asText("No response content");
                } else {
                    return String.format("HTTP Error %d: %s", 
                        response.code(), 
                        response.body() != null ? response.body().string() : "No error details");
                }
            }
            
        } catch (Exception e) {
            return "Custom request failed: " + e.getMessage() + 
                   "\n\nPlease check:\n" +
                   "1. API key is correct\n" +
                   "2. Server allows your IP address\n" +
                   "3. Model name is correct\n" +
                   "4. Server configuration allows external access";
        }
    }

    
    @Override
    public String getModelName() {
        return config.getModelName();
    }
    public OllamaChatModel getChatModel() {
        return chatModel;
    }
}
