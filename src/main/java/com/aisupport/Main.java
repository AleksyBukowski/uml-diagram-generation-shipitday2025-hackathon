package com.aisupport;

import com.aisupport.config.AIConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;
import com.aisupport.service.OllamaService;

import java.util.Scanner;
public class Main {
    
    public static void main(String[] args) {
        // Load configuration and create service
        AIConfig config = new AIConfig();
        AIService aiService = AIServiceFactory.createService(config);

        // Send a message
        String response = aiService.chat("What is the capital of France?");
        System.out.println(response);
    }
}
