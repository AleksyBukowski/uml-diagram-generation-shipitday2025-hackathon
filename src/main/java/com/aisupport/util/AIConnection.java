package com.aisupport.util;

import com.aisupport.config.AIBasicConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;

public class AIConnection {
    public static AIService provideService(AIBasicConfig config) {
        return AIServiceFactory.createService(config); // Placeholder for actual implementation
    }

    public static AIService provideOllamaGemma2Service(){
        AIBasicConfig configOllama = new AIBasicConfig("ollama",
                "aie93JaTv1GW1AP4IIUSqeecV22HgpcQ6WlgWNyfx2HflkY5hTw19JDbT90ViKcZaZ6lpjOo3YIGgpkG7Zb8jEKvdM5Ymnq9jPm79osLppCebwJ7WdWTwWq3Rf15NDxm",
                "http://20.185.83.16:8080/",
                "gemma2", 2048, 0.7);
        return AIConnection.provideService(configOllama);
    }

    public static AIService provideVllmLlama38bService(){
        AIBasicConfig config = new AIBasicConfig("vllm",
                "dummy",
                "http://9.169.65.166:8080/v1",
                "llama3_8b_it", 2048, 0.7);
        return AIConnection.provideService(config);
    }

    public static AIService provideOllamaService(String model){
        AIBasicConfig configOllama = new AIBasicConfig("ollama",
                "aie93JaTv1GW1AP4IIUSqeecV22HgpcQ6WlgWNyfx2HflkY5hTw19JDbT90ViKcZaZ6lpjOo3YIGgpkG7Zb8jEKvdM5Ymnq9jPm79osLppCebwJ7WdWTwWq3Rf15NDxm",
                "http://20.185.83.16:8080/",
                model, 2048, 0.7);
        return AIConnection.provideService(configOllama);
    }

    public static AIService provideVllmService(String model){
        AIBasicConfig configVllm = new AIBasicConfig("vllm",
                "dummy",
                "http://9.169.65.166:8080/v1",
                model, 2048, 0.7);
        return AIConnection.provideService(configVllm);
    }



}
