package com.aisupport;

import com.aisupport.config.AIConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic test class for AI services
 */
public class AIServiceTest {
    
    @Test
    public void testConfigurationLoading() {
        AIConfig config = new AIConfig();
        assertNotNull(config.getEngineType());
        assertNotNull(config.getApiUrl());
        assertNotNull(config.getModelName());
        assertTrue(config.getMaxTokenLimit() > 0);
    }
    
    @Test
    public void testServiceCreation() {
        AIConfig config = new AIConfig();
        AIService service = AIServiceFactory.createService(config);
        assertNotNull(service);
        assertEquals(config.getModelName(), service.getModelName());
    }
    
    @Test
    public void testOllamaService() {
        AIConfig config = new AIConfig();
        if (config.isOllama()) {
            AIService service = AIServiceFactory.createService(config);
            assertNotNull(service);
            // Note: This test requires a running Ollama server
            // Uncomment the following line if you want to test actual communication
            // assertNotNull(service.sendMessage("Hello"));
        }
    }
}
