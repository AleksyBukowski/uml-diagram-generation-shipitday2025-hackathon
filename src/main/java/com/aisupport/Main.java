package com.aisupport;

import com.aisupport.config.AIConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;
import com.aisupport.service.OllamaService;

import java.util.Scanner;

/**
 * Main application class for AI Support
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== AI Support Application ===");
        System.out.println("Connecting to AI models...");
        
        try {
            // Load configuration
            AIConfig config = new AIConfig();
            System.out.println("Engine Type: " + config.getEngineType());
            System.out.println("Model: " + config.getModelName());
            System.out.println("API URL: " + config.getApiUrl());
            
            // Create AI service
            AIService aiService = AIServiceFactory.createService(config);
            System.out.println("✓ Successfully connected to " + config.getEngineType() + " model: " + config.getModelName());
           // String res = ((OllamaService) aiService).sendMessage("What is capital of poland?");
            // Interactive chat loop
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n=== Chat Interface ===");
            System.out.println("Type your message (or 'quit' to exit):");
            
            while (true) {
                System.out.print("\nYou: ");
                String userInput = scanner.nextLine().trim();
                
                if ("quit".equalsIgnoreCase(userInput) || "exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Goodbye!");
                    break;
                }
                
                if (userInput.isEmpty()) {
                    continue;
                }
                
                System.out.print("AI: ");
                long startTime = System.currentTimeMillis();
                String response = aiService.sendMessage(userInput);
                long endTime = System.currentTimeMillis();
                
                System.out.println(response);
                System.out.println("(Response time: " + (endTime - startTime) + "ms)");
            }
            
            scanner.close();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
