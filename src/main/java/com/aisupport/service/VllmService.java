package com.aisupport.service;

import com.aisupport.config.AIConfig;
import com.aisupport.config.CustomHttpClient;
import com.aisupport.config.CustomHttpClientBuilder;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Service implementation for VLLM AI models
 * Uses OpenAI-compatible API interface
 */
public class VllmService implements AIService {
    
    private final AIConfig config;
    private final OpenAiChatModel chatModel;
    
    public VllmService(AIConfig config) {
        this.config = config;
        this.chatModel = createChatModel();
    }
    
    private OpenAiChatModel createChatModel() {
        HttpClient.Builder client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1);
        CustomHttpClientBuilder customHttpClientBuilder = CustomHttpClient.builder().httpClientBuilder(client);
             return OpenAiChatModel.builder()
                .httpClientBuilder(customHttpClientBuilder)
                .baseUrl(config.getApiUrl())
                .customHeaders(Map.of("Authorization", "Bearer " + config.getApiKey()))
                .modelName(config.getModelName())
                .temperature(config.getTemperature())
                .timeout(Duration.of(5, ChronoUnit.MINUTES))
                .build();
    }
    
    @Override
    public String sendMessage(String message) {
        try {
            String response = chatModel.chat(message);
            return response;
        } catch (Exception e) {
            return "Error communicating with VLLM: " + e.getMessage();
        }
    }
    
    public ChatModel getChatModel() {
        return chatModel;
    }
    
    @Override
    public String getModelName() {
        return config.getModelName();
    }
}
