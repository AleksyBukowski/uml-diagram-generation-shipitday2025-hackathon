package com.aisupport.config;

public class AIBasicConfig {
    protected String engineType;
    protected String apiKey;
    protected String apiUrl;
    protected String modelName;
    protected int maxTokenLimit;
    protected double temperature;

    public AIBasicConfig(String engineType, String apiKey, String apiUrl, String modelName, int maxTokenLimit, double temperature) {
        this.engineType = engineType;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.modelName = modelName;
        this.maxTokenLimit = maxTokenLimit;
        this.temperature = temperature;
    }

    public AIBasicConfig() {
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
