package com.aisupport.service;

import com.aisupport.config.AIBasicConfig;
import com.aisupport.config.AIConfig;

/**
 * Factory class for creating AI services based on configuration
 */
public class AIServiceFactory {
    
    /**
     * Creates an AI service based on the configuration
     * @param config The AI configuration
     * @return An appropriate AI service implementation
     * @throws IllegalArgumentException if the engine type is not supported
     */
    public static AIService createService(AIBasicConfig config) {
        if (config.isOllama()) {
            return new OllamaService(config);
        } else if (config.isVllm()) {
            return new VllmService(config);
        } else {
            throw new IllegalArgumentException("Unsupported AI engine type: " + config.getEngineType());
        }
    }
}
