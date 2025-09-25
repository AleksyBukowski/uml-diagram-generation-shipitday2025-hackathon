package com.aisupport.service;

import com.aisupport.config.AIBasicConfig;
import com.aisupport.config.AIConfig;
import com.aisupport.config.CustomHttpClient;
import com.aisupport.config.CustomHttpClientBuilder;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for VLLM AI models
 * Uses OpenAI-compatible API interface
 */
public class VllmService implements AIService {

    private final AIBasicConfig config;
    private final OpenAiChatModel chatModel;

    public VllmService(AIBasicConfig config) {
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

    public ChatModel getChatModel() {
        return chatModel;
    }

    @Override
    public String getModelName() {
        return config.getModelName();
    }

    @Override
    public ChatResponse chat(ChatRequest chatRequest) {
        String messages = chatRequest.messages().stream()
                .map(message -> {
                    if (message instanceof UserMessage) {
                        return ((UserMessage) message).singleText();
                    } else if (message instanceof AiMessage) {
                        return ((AiMessage) message).text();
                    } else if (message instanceof SystemMessage) {
                        return ((SystemMessage) message).text();
                    } else {
                        // For any other message type, convert to string representation
                        return message.toString();
                    }
                })
                .collect(Collectors.joining(" "));
        UserMessage userMessage = new UserMessage(messages);
        ChatRequest castedRequest = ChatRequest.builder()
                .messages(userMessage)
                .build();
        ChatResponse response = chatModel.chat(castedRequest);
        return response;
    }

    @Override
    public ChatResponse chat(ChatMessage... messages) {
        // For VLLM, we need to ensure proper conversation role alternation
        // VLLM expects: user -> assistant -> user -> assistant...
        // System messages are not supported, so we need to handle them differently

        List<ChatMessage> processedMessages = new ArrayList<>();
        StringBuilder systemInstructions = new StringBuilder();

        for (ChatMessage message : messages) {
            if (message instanceof SystemMessage) {
                // Collect system instructions to prepend to the first user message
                if (systemInstructions.length() > 0) {
                    systemInstructions.append(" ");
                }
                systemInstructions.append(((SystemMessage) message).text());
            } else if (message instanceof UserMessage) {
                // If this is the first user message and we have system instructions, prepend them
                if (systemInstructions.length() > 0) {
                    String originalText = ((UserMessage) message).singleText();
                    String combinedText = systemInstructions.toString() + "\n\n" + originalText;
                    processedMessages.add(UserMessage.from(combinedText));
                    systemInstructions.setLength(0); // Clear after using
                } else {
                    processedMessages.add(message);
                }
            } else {
                processedMessages.add(message);
            }
        }

        // If we have system instructions but no user message, create one
        if (systemInstructions.length() > 0 && processedMessages.isEmpty()) {
            processedMessages.add(UserMessage.from(systemInstructions.toString()));
        }

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(processedMessages)
                .build();

        return chatModel.chat(chatRequest);
    }

    @Override
    public ChatResponse chat(List<ChatMessage> chatMessages) {
        // For VLLM, we need to ensure proper conversation role alternation
        // VLLM expects: user -> assistant -> user -> assistant...
        // System messages are not supported, so we need to handle them differently

        List<ChatMessage> processedMessages = new ArrayList<>();
        StringBuilder systemInstructions = new StringBuilder();

        for (ChatMessage message : chatMessages) {
            if (message instanceof SystemMessage) {
                // Collect system instructions to prepend to the first user message
                if (systemInstructions.length() > 0) {
                    systemInstructions.append(" ");
                }
                systemInstructions.append(((SystemMessage) message).text());
            } else if (message instanceof UserMessage) {
                // If this is the first user message and we have system instructions, prepend them
                if (systemInstructions.length() > 0) {
                    String originalText = ((UserMessage) message).singleText();
                    String combinedText = systemInstructions.toString() + "\n\n" + originalText;
                    processedMessages.add(UserMessage.from(combinedText));
                    systemInstructions.setLength(0); // Clear after using
                } else {
                    processedMessages.add(message);
                }
            } else {
                processedMessages.add(message);
            }
        }

        // If we have system instructions but no user message, create one
        if (systemInstructions.length() > 0 && processedMessages.isEmpty()) {
            processedMessages.add(UserMessage.from(systemInstructions.toString()));
        }

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(processedMessages)
                .build();

        return chatModel.chat(chatRequest);
    }

    @Override
    public String chat(String message) {
        // For VLLM, we need to ensure proper conversation role alternation
        // Create a conversation with system message, user message, and assistant response
        UserMessage userMessage = UserMessage.from(message);

        // Create a chat request with proper conversation structure
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .build();

        ChatResponse response = chatModel.chat(chatRequest);
        return response.aiMessage().text();
    }
}