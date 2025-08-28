# AI Support - Java Maven Project

A Java application that allows you to send messages and get responses from both Ollama and VLLM models using LangChain4j.

## Features

- **Ollama Integration**: Connect to Ollama models using the Ollama API with enhanced authentication
- **VLLM Integration**: Connect to VLLM servers using OpenAI-compatible API interface
- **Configuration Management**: Easy configuration through properties file with temperature control
- **LangChain4j**: Built with the latest LangChain4j library for AI model interactions
- **Enhanced Authentication**: Improved API key handling with custom headers for Ollama
- **Maven Project**: Standard Maven project structure with proper dependencies

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Access to Ollama or VLLM server

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/aisupport/
│   │       ├── config/
│   │       │   └── AIConfig.java          # Configuration loader with temperature support
│   │       ├── service/
│   │       │   ├── AIService.java         # Service interface
│   │       │   ├── OllamaService.java     # Ollama implementation with enhanced auth
│   │       │   ├── VllmService.java       # VLLM implementation
│   │       │   └── AIServiceFactory.java  # Service factory
│   │       └── Main.java                  # Main application
│   └── resources/
│       └── ai-config.properties           # Configuration file
└── test/
    └── java/
        └── com/aisupport/
            └── AIServiceTest.java         # Test class
```

## Configuration

Edit `src/main/resources/ai-config.properties` to configure your AI engine:

### For Ollama:
```properties
engine.ai.type=ollama
engine.ai.apiKey=your_api_key_here
engine.ai.apiUrl=http://your_ollama_server:port/
engine.ai.modelName=your_model_name
engine.ai.maxTokenLimit=2048
engine.ai.temperature=0.7
```

### For VLLM:
```properties
engine.ai.type=vllm
engine.ai.apiKey=your_api_key_here
engine.ai.apiUrl=http://your_vllm_server:port/v1
engine.ai.modelName=your_model_name
engine.ai.maxTokenLimit=2048
engine.ai.temperature=0.7
```

### Configuration Parameters:
- **engine.ai.type**: Choose between `ollama` or `vllm`
- **engine.ai.apiKey**: Your API key for authentication
- **engine.ai.apiUrl**: Base URL of your AI server
- **engine.ai.modelName**: Name of the model to use
- **engine.ai.maxTokenLimit**: Maximum tokens for responses (default: 2048)
- **engine.ai.temperature**: Controls randomness in responses (0.0 = deterministic, 1.0 = very random, default: 0.7)

## Building the Project

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package

# Run the application
mvn exec:java -Dexec.mainClass="com.aisupport.Main"
```

## Running the Application

### Option 1: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.aisupport.Main"
```

### Option 2: Using JAR file
```bash
# First build the JAR
mvn package

# Then run it
java -jar target/ai-support-1.0.0.jar
```

### Option 3: From IDE
Run the `Main.java` class directly from your IDE.

## Usage

1. **Start the application**: The application will load configuration and connect to the specified AI model
2. **Interactive chat**: Type your messages and receive AI responses
3. **Exit**: Type 'quit' or 'exit' to close the application

Example session:
```
=== AI Support Application ===
Connecting to AI models...
Engine Type: ollama
Model: gemma2
API URL: http://20.185.83.16:8080/
✓ Successfully connected to ollama model: gemma2

=== Chat Interface ===
Type your message (or 'quit' to exit):

You: Hello, how are you?
AI: Hello! I'm doing well, thank you for asking. How can I help you today?
(Response time: 1250ms)

You: quit
Goodbye!
```

## Switching Between Engines

To switch between Ollama and VLLM:

1. Edit `src/main/resources/ai-config.properties`
2. Comment out the current engine configuration
3. Uncomment the desired engine configuration
4. Restart the application

## Recent Updates

### Enhanced Ollama Integration
- **Improved Authentication**: Added custom authorization headers for better API key handling
- **Temperature Control**: Configurable temperature parameter for response creativity
- **Method Updates**: Updated to use `chat()` method instead of deprecated `generate()` method
- **Better Error Handling**: Enhanced error messages and fallback mechanisms

### Configuration Enhancements
- **Temperature Support**: Added temperature configuration for fine-tuning AI responses
- **Flexible Settings**: More granular control over AI model behavior

## Dependencies

- **LangChain4j**: Core AI framework
- **LangChain4j Ollama**: Ollama integration
- **LangChain4j OpenAI**: VLLM compatibility
- **OkHttp**: HTTP client
- **Jackson**: JSON processing
- **SLF4J**: Logging
- **JUnit 5**: Testing

## Troubleshooting

### Connection Issues
- Verify your server is running and accessible
- Check the API URL and port
- Ensure the model name is correct
- Verify API key if required

### 403 Forbidden Error
If you're getting a 403 Forbidden error, try these steps:

1. **Test Connection**: Run the connection tester to diagnose issues:
   ```bash
   mvn exec:java -Dexec.mainClass="com.aisupport.util.ConnectionTester"
   ```

2. **Check Authentication**:
   - Verify the API key is correct
   - Ensure the API key has proper permissions
   - Check if the server requires specific authentication headers
   - The application now automatically adds Authorization headers for Ollama

3. **Network Issues**:
   - Verify your IP address is whitelisted on the server
   - Check firewall settings
   - Ensure the server allows external connections

4. **Server Configuration**:
   - Check if the Ollama server requires authentication
   - Verify the server is configured to accept external requests
   - Check nginx/load balancer configuration if applicable

### Model Issues
- Make sure the specified model is available on your server
- Check server logs for model loading errors
- Verify model compatibility with your server version

### Configuration Issues
- Ensure the properties file is in the correct location
- Check property names and values
- Verify the engine type is either 'ollama' or 'vllm'
- Temperature values should be between 0.0 and 1.0

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the MIT License.
