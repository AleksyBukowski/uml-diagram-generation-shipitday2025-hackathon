package com.aisupport.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for AI engine settings
 */
public class AIConfig {
    private static final String CONFIG_FILE = "ai-config.properties";
    
    private String engineType;
    private String apiKey;
    private String apiUrl;
    private String modelName;
    private int maxTokenLimit;
    private double temperature;

    public AIConfig() {
        loadConfig();
    }
    
    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
                this.engineType = props.getProperty("engine.ai.type", "ollama");
                this.apiKey = props.getProperty("engine.ai.apiKey", "");
                this.apiUrl = props.getProperty("engine.ai.apiUrl", "");
                this.modelName = props.getProperty("engine.ai.modelName", "");
                this.maxTokenLimit = Integer.parseInt(props.getProperty("engine.ai.maxTokenLimit", "2048"));
                this.temperature = Double.parseDouble(props.getProperty("engine.ai.temperature", "0.7"));
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    public String getEngineType() { return engineType; }
    public String getApiKey() { return apiKey; }
    public String getApiUrl() { return apiUrl; }
    public String getModelName() { return modelName; }
    public int getMaxTokenLimit() { return maxTokenLimit; }
    public double getTemperature() { return temperature; }

    public boolean isOllama() {
        return "ollama".equalsIgnoreCase(engineType);
    }
    
    public boolean isVllm() {
        return "vllm".equalsIgnoreCase(engineType);
    }
}
