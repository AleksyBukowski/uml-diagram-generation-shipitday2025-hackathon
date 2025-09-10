package com.aisupport.service;


import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

import java.util.List;

public interface AIService {
   String getModelName();
   ChatResponse chat(ChatRequest chatRequest);
   ChatResponse chat(ChatMessage... messages);
   ChatResponse chat(List<ChatMessage> chatMessages);
   String chat(String message);

}
