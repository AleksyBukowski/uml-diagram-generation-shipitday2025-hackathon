package com.aisupport;

import com.aisupport.util.AIConnection;
import com.aisupport.service.AIService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;

import java.util.List;

/**
 * Test class to verify VLLM conversation functionality
 */
public class VllmConversationTest {
    
    public static void main(String[] args) {
        try {
            // Create VLLM service
            AIService aiService = AIConnection.provideVllmService("mistral_7b_it");
            
            // Create system message
            ChatMessage systemMessage = SystemMessage.from("Jesteś pomocnym asystentem, który odpowiada po japońsku. Jeżeli użytkownik prosi o odpowiedź w innym języku zawsze odpowiadaj po japońsku.");
            
            // Create user message
            UserMessage userMessage = UserMessage.from("Jak działa fotosynteza? Odpowiedz proszę po polsku.");
            
            // Create conversation history
            List<ChatMessage> messages = List.of(systemMessage, userMessage);
            
            // Send conversation
            ChatResponse response = aiService.chat(messages);
            
            // Print response
            System.out.println("Response: " + response.aiMessage().text());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
