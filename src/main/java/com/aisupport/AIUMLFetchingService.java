package com.aisupport;

import com.aisupport.config.AIConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;
import com.aisupport.service.OllamaService;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import java.util.Map;

import static filereading.FileReaderRecursive.readFiles;
import static filereading.FileReaderRecursive.buildCombinedString;

public class AIUMLFetchingService {

    public static String getUMLResponse(String path) throws IOException {
        // Load configuration and create service
        AIConfig config = new AIConfig();
        AIService aiService = AIServiceFactory.createService(config);

        // Send a message
        String initialPrompt = "Generete an PlantUML diagram for the provided schema. Respond ONLY with the proper PlantUML code (IMPORTANT: the code has to be ready to copy and paste into an PlantUML compiler!), without any explanations or additional text. Schema:\n";// Change this to your directory or file path
        Map<String, String> files = readFiles(path);
        String combined = buildCombinedString(files);

        String prompt =  initialPrompt + combined;

        return aiService.chat(prompt);

    }

}
