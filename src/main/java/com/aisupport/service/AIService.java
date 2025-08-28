package com.aisupport.service;


/**
 * Service interface for AI model interactions
 */
public interface AIService {
    
    /**
     * Send a message to the AI model and get a response
     * @param message The message to send
     * @return The AI model's response
     */
    String sendMessage(String message);
    

    /**
     * Get the current model name being used
     * @return The model name
     */
    String getModelName();
}
