package com.aisupport.example;

import com.aisupport.config.AIConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;
import com.aisupport.service.OllamaService;
import com.aisupport.util.AIConnection;

/**
 * Example class demonstrating how to use the AI services programmatically
 */
public class ExampleUsage {
    
    public static void main(String[] args) {
        // Load configuration
        AIConfig config = new AIConfig();
        
        // Create AI service
        AIService aiService = AIServiceFactory.createService(config);
        
        System.out.println("Using model: " + aiService.getModelName());
        System.out.println("Engine type: " + config.getEngineType());
        
        // Example 1: Simple question
        String question = "What is the capital of France?";
        System.out.println("\nQuestion: " + question);
        String response = aiService.sendMessage(question);
        System.out.println("Response: " + response);
        
        // Example 2: Creative writing
        String creativePrompt = "Write a short poem about artificial intelligence";
        System.out.println("\nPrompt: " + creativePrompt);
        String creativeResponse = aiService.sendMessage(creativePrompt);
        System.out.println("Response: " + creativeResponse);
        
        // Example 3: Code generation
        String codePrompt = "Write a Java function to calculate factorial";
        System.out.println("\nPrompt: " + codePrompt);
        String codeResponse = aiService.sendMessage(codePrompt);
        System.out.println("Response: " + codeResponse);
    }
    
    /**
     * Example method showing how to use the service in your own code
     */
    public static String getAIResponse(String message) {
        try {
            AIConfig config = new AIConfig();
            AIService aiService = AIServiceFactory.createService(config);
            return aiService.sendMessage(message);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Example method for batch processing
     */
    public static void processBatchMessages(String[] messages) {
        AIConfig config = new AIConfig();
        AIService aiService = AIServiceFactory.createService(config);
        
        for (int i = 0; i < messages.length; i++) {
            System.out.println("Processing message " + (i + 1) + ": " + messages[i]);
            String response = aiService.sendMessage(messages[i]);
            System.out.println("Response: " + response);
            System.out.println("---");
        }
    }
}
