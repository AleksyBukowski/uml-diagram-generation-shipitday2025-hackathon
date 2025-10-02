package io.github.adamw7.genaiAgentCore.domain;

import com.aisupport.config.AIBasicConfig;
import com.aisupport.service.AIService;
import com.aisupport.util.AIConnection;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class AIServiceTest {
private static final String OLLAMA_ENGINE_NAME = "ollama";
private static final String VLLM_ENGINE_NAME = "vllm";
private static final String OLLAMA_DEFAULT_MODEL = "gemma2";
private static final String VLLM_DEFAULT_MODEL = "mistral_7b_it";
private static final String OLLAMA_URL = "<to be put here>";
private static final String VLLM_URL = "<to be put here>";
private static final String OLLAMA_API_KEY = "<to be put here>";
private static final String VLLM_API_KEY = "<to be put here>";


    @Test
    public void scenario1() {
        // Simple use of deafault ollama or VLM
        String question = "What is the capital of France?";

        AIService ollamaService = AIConnection.provideOllamaGemma2Service();
        String ollamaResponse = ollamaService.chat(question);
        System.out.println("Ollama response -> \n" + ollamaResponse);

        AIService vllmService = AIConnection.provideVllmBasicService();
        String vllmResponse = vllmService.chat(question);
        System.out.println("VLLM response -> \n" + vllmResponse);

    }

    @Test
    public void scenario2() {
        // Simple use of ollama or VLM with defined model
        String question = "Can people colonize the Moon?";

        AIService ollamaService = AIConnection.provideOllamaService("gemma2");
        String ollamaResponse = ollamaService.chat(question);
        System.out.println("Ollama response -> \n" + ollamaResponse);

        AIService vllmService = AIConnection.provideVllmService("mistral_7b_it");
        String vllmResponse = vllmService.chat(question);
        System.out.println("VLLM response -> \n" + vllmResponse);

    }

    @Test
    public void scenario3() {
        // BasicConfiguration passed for specific model
        String question = "Could You create short joke about star conquering";


        AIBasicConfig configOllama = new AIBasicConfig(OLLAMA_ENGINE_NAME, OLLAMA_API_KEY, OLLAMA_URL, OLLAMA_DEFAULT_MODEL, 2048, 0.7);
        AIBasicConfig configVLLM = new AIBasicConfig(VLLM_ENGINE_NAME, VLLM_API_KEY, VLLM_URL, VLLM_DEFAULT_MODEL, 2048, 0.7);


        AIService ollamaService = AIConnection.provideService(configOllama);
        String ollamaResponse = ollamaService.chat(question);
        System.out.println("Ollama response -> \n" + ollamaResponse);

        AIService vllmService = AIConnection.provideService(configVLLM);
        String vllmResponse = vllmService.chat(question);
        System.out.println("VLLM response -> \n" + vllmResponse);
    }


    @Test
    public void scenario4() {
        // Using different input for chat

        AIService ollamaService = AIConnection.provideOllamaGemma2Service();
        AIService vllmService = AIConnection.provideVllmBasicService();

        SystemMessage systemMessage = new SystemMessage("You are a helpful assistant that translates English to French.");
        UserMessage userMessage = new UserMessage("Translate the following English text to French: 'Hello, how are you?'");

        List<ChatMessage> listOfMessages = List.of(systemMessage, userMessage);

        ChatResponse ollamaResponse = ollamaService.chat(listOfMessages);
        System.out.println("Ollama response -> " + ollamaResponse.aiMessage().text());

        ChatResponse vllmResponse = vllmService.chat(listOfMessages);
        System.out.println("VLLM response -> " + vllmResponse.aiMessage().text());


        ChatRequest request = ChatRequest.builder()
                .messages(List.of(systemMessage, userMessage))
                .build();

        ChatResponse ollamaResponseRequest = ollamaService.chat(request);
        System.out.println("Ollama response -> \n" + ollamaResponseRequest.aiMessage().text());

        ChatResponse vllmResponseRequest = vllmService.chat(request);
        System.out.println("VLLM response -> \n" + vllmResponse.aiMessage().text());
    }


    @Test
    public void scenario5() {
        // Use of multi modal model with image
        try {
            // Load image from resources
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("pic1.jpg");
            if (imageStream == null) {
                System.err.println("Could not find pic1.jpg in resources. Make sure the file is in src/main/resources/");
                return;
            }

            // Convert image to base64
            byte[] imageBytes = imageStream.readAllBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            imageStream.close();

            // Create multimodal message with image and prompt
            String prompt = "Please describe what you see in this image in detail.";
            ImageContent imageContent = ImageContent.from(base64Image, "image/jpeg");
            TextContent textContent = TextContent.from(prompt);
            UserMessage userMessage = UserMessage.from(List.of(
                textContent,
                imageContent
            ));

            // Send to Ollama LLaVA model
            AIService ollamaService = AIConnection.provideOllamaService("llava");
            ChatResponse ollamaResponse = ollamaService.chat(userMessage);
            System.out.println("Ollama LLaVA response -> \n" + ollamaResponse.aiMessage().text());

        } catch (IOException e) {
            System.err.println("Error loading or processing image: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

