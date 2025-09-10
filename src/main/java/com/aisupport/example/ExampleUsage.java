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
        String response = aiService.chat(question);
        System.out.println("Response: " + response);
        
        // Example 2: Creative writing
        String creativePrompt = "Write a short poem about artificial intelligence";
        System.out.println("\nPrompt: " + creativePrompt);
        String creativeResponse = aiService.chat(creativePrompt);
        System.out.println("Response: " + creativeResponse);
        
        // Example 3: Code generation
        String codePrompt = "Write a Java function to calculate factorial";
        System.out.println("\nPrompt: " + codePrompt);
        String codeResponse = aiService.chat(codePrompt);
        System.out.println("Response: " + codeResponse);
    }
}
