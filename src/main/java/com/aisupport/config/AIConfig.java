package com.aisupport.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for AI engine settings
 */
public class AIConfig extends AIBasicConfig{
    private static final String CONFIG_FILE = "ai-config.properties";


    public AIConfig() {
        loadConfig();
    }
    
    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
                engineType = props.getProperty("engine.ai.type", "ollama");
                apiKey = props.getProperty("engine.ai.apiKey", "");
                apiUrl = props.getProperty("engine.ai.apiUrl", "");
                modelName = props.getProperty("engine.ai.modelName", "");
                maxTokenLimit = Integer.parseInt(props.getProperty("engine.ai.maxTokenLimit", "2048"));
                temperature = Double.parseDouble(props.getProperty("engine.ai.temperature", "0.7"));
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
}
