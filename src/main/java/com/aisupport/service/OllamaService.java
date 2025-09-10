package com.aisupport.service;

import com.aisupport.config.AIBasicConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public String getModelName() {
        return config.getModelName();
    }

    @Override
    public ChatResponse chat(ChatRequest chatRequest) {
        return this.chatModel.chat(chatRequest);
    }

    @Override
    public ChatResponse chat(ChatMessage... messages) {
        return this.chatModel.chat(messages);
    }

    @Override
    public ChatResponse chat(List<ChatMessage> chatMessages) {
        return this.chatModel.chat(chatMessages);
    }

    @Override
    public String chat(String message) {
        return this.chatModel.chat(message);
    }

    public OllamaChatModel getChatModel() {
        return chatModel;
    }
}
