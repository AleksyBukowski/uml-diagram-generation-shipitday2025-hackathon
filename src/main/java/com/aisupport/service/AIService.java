package com.aisupport.service;


import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

import java.util.List;

/**
 * Service interface for AI model interactions
 */
public interface AIService {

    /**
     * Get the current model name being used
     * @return The model name
     */
    String getModelName();

   ChatResponse chat(ChatRequest chatRequest);
   ChatResponse chat(ChatMessage... messages);
   ChatResponse chat(List<ChatMessage> chatMessages);
   String chat(String message);

}
