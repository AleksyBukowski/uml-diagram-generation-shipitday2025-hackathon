package com.aisupport.util;

import com.aisupport.config.AIBasicConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;

public class AIConnection {

    private static final String OLLAMA_ENGINE_NAME = "ollama";
    private static final String VLLM_ENGINE_NAME = "vllm";
    private static final String OLLAMA_DEFAULT_MODEL = "gemma2";
    private static final String VLLM_DEFAULT_MODEL = "mistral_7b_it";
    private static final String OLLAMA_URL = "...";
    private static final String VLLM_URL = "...";
    private static final String OLLAMA_API_KEY = "...";
    private static final String VLLM_API_KEY = "dummy";

    public static AIService provideService(AIBasicConfig config) {
        return AIServiceFactory.createService(config); // Placeholder for actual implementation
    }

    public static AIService provideOllamaGemma2Service(){
        AIBasicConfig configOllama = new AIBasicConfig(OLLAMA_ENGINE_NAME,
                OLLAMA_API_KEY,
                OLLAMA_URL,
                OLLAMA_DEFAULT_MODEL, 2048, 0.7);
        return AIConnection.provideService(configOllama);
    }

    public static AIService provideVllmBasicService(){
        AIBasicConfig config = new AIBasicConfig(VLLM_ENGINE_NAME,
                VLLM_API_KEY,
                VLLM_URL,
                VLLM_DEFAULT_MODEL, 2048, 0.7);
        return AIConnection.provideService(config);
    }

    public static AIService provideOllamaService(String model){
        AIBasicConfig configOllama = new AIBasicConfig(OLLAMA_ENGINE_NAME,
                OLLAMA_API_KEY,
                OLLAMA_URL,
                model, 2048, 0.7);
        return AIConnection.provideService(configOllama);
    }

    public static AIService provideVllmService(String model){
        AIBasicConfig configVllm = new AIBasicConfig(VLLM_ENGINE_NAME,
                VLLM_API_KEY,
                VLLM_URL,
                model, 2048, 0.7);
        return AIConnection.provideService(configVllm);
    }



}
