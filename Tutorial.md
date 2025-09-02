# AI Support 

To use project as a support You can view how connection is established for Ollama or VLLM.
Alternatively, you can use this as a dependency to build your own project.

## Features

- Connect to Ollama or VLLM models
- Use AIConnection to interact with models
- You can use basic models or pass it to AIConnection for advanced model discovery and interaction
    - config see AIBasicConfig.java
    - connection see AIConnection.java

- Example usage:

  ChatMessage message = UserMessage.from("Write a poem about Spring Boot in Java");
  
  ChatRequest  newRequest = ChatRequest.builder()
                            .messages(message)
                            .build();
  ChatRequest chatRequest = ChatRequest.builder()
                            .messages(message)
                            .build();
  ChatResponse response =   ((OllamaService)AIConnection.provideOllamaGemma2Service()).getChatModel().doChat(chatRequest);


### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Access to Ollama or VLLM server


####

- Do `mvn clean install` to build the project
- Use dependancy in your own project

```xml
        <dependency>
            <groupId>com.aisupport</groupId>
            <artifactId>ai-support</artifactId>
            <version>1.0.0</version>
        </dependency>
