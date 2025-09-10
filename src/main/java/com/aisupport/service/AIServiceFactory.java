package com.aisupport.service;

import com.aisupport.config.AIBasicConfig;
public class AIServiceFactory {

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
