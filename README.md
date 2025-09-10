# AI Support - Java Maven Project

A comprehensive Java application that provides seamless integration with both Ollama and VLLM models using LangChain4j. This project serves as both a standalone application and a reusable library for AI model interactions.

## Features

- **Multi-Engine Support**: Connect to both Ollama and VLLM models with unified interface
- **Configuration Management**: Easy configuration through properties file with temperature control
- **LangChain4j Integration**: Built with LangChain4j for AI model interactions:
  - Ollama integration via `langchain4j-ollama`
  - OpenAI-compatible API (VLLM support) via `langchain4j-open-ai`
  - Core LangChain4j functionality
- **Custom HTTP Client**: Custom HTTP client implementation for advanced configurations
- **Connection Testing**: Built-in utilities to test and diagnose connection issues
- **Programmatic API**: Clean service interface for easy integration into other projects
- **Example Usage**: Basic examples for different use cases
- **Maven Project**: Standard Maven project structure with proper dependency management

## Quick Start

1. **Clone and build**:
   ```bash
   git clone <repository-url>
   cd shipItDay2025repo
   mvn clean compile
   ```

2. **Configure your AI server**:
   Edit `src/main/resources/ai-config.properties` with your server details

3. **Run the application**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.aisupport.Main"
   ```

4. **Test your connection**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.aisupport.util.ConnectionTester"
   ```

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
│   │       │   ├── AIBasicConfig.java     # Basic configuration interface
│   │       │   ├── AIConfig.java          # Configuration loader with temperature support
│   │       │   ├── CustomHttpClient.java  # Custom HTTP client for advanced configurations
│   │       │   └── CustomHttpClientBuilder.java # HTTP client builder
│   │       ├── example/
│   │       │   └── ExampleUsage.java      # Comprehensive usage examples
│   │       ├── service/
│   │       │   ├── AIService.java         # Service interface
│   │       │   ├── OllamaService.java     # Ollama implementation with enhanced auth
│   │       │   ├── VllmService.java       # VLLM implementation
│   │       │   └── AIServiceFactory.java  # Service factory
│   │       ├── util/
│   │       │   ├── AIConnection.java      # Advanced connection utility
│   │       │   └── ConnectionTester.java  # Connection testing and diagnostics
│   │       └── Main.java                  # Main application entry point
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

### Standalone Application

1. **Start the application**: The application will load configuration and connect to the specified AI model
2. **Interactive chat**: Type your messages and receive AI responses
3. **Exit**: Type 'quit' or 'exit' to close the application

Example session:
```
=== AI Support Application ===
Connecting to AI models...
Engine Type: vllm
Model: llama3_8b_it
API URL: http://9.169.65.166:8080/v1
✓ Successfully connected to vllm model: llama3_8b_it

=== Chat Interface ===
Type your message (or 'quit' to exit):

You: Hello, how are you?
AI: Hello! I'm doing well, thank you for asking. How can I help you today?
(Response time: 1250ms)

You: quit
Goodbye!
```

### Programmatic Usage

The application can also be used as a library in your own projects:

#### Basic Usage
```java
import com.aisupport.config.AIConfig;
import com.aisupport.service.AIService;
import com.aisupport.service.AIServiceFactory;

// Load configuration and create service
AIConfig config = new AIConfig();
AIService aiService = AIServiceFactory.createService(config);

// Send a message
String response = aiService.chat("What is the capital of France?");
System.out.println(response);
```

#### Advanced Usage with AIConnection
```java
import com.aisupport.util.AIConnection;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

// Use AIConnection for pre-configured services
AIService ollamaService = AIConnection.provideOllamaGemma2Service();
String response = ollamaService.chat("Write a poem about Spring Boot in Java");

// Or use with custom models
AIService customOllamaService = AIConnection.provideOllamaService("llama2");
AIService customVllmService = AIConnection.provideVllmService("llama3_8b_it");
```

#### Using Example Usage Class
```java
import com.aisupport.example.ExampleUsage;

// Simple usage
String response = ExampleUsage.getAIResponse("What is machine learning?");

// Or run the example directly
ExampleUsage.main(new String[]{});
```

### Connection Testing

Test your connection before using the application:

```bash
# Test Ollama connection
mvn exec:java -Dexec.mainClass="com.aisupport.util.ConnectionTester"

# Test specific configuration
mvn exec:java -Dexec.mainClass="com.aisupport.example.ExampleUsage"
```

## Switching Between Engines

To switch between Ollama and VLLM:

1. Edit `src/main/resources/ai-config.properties`
2. Comment out the current engine configuration
3. Uncomment the desired engine configuration
4. Restart the application

## Using as a Dependency

This project can be used as a dependency in your own Maven projects:

### Installation

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Add to your pom.xml**:
   ```xml
   <dependency>
       <groupId>com.aisupport</groupId>
       <artifactId>ai-support</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

3. **Use in your code**:
   ```java
   import com.aisupport.config.AIConfig;
   import com.aisupport.service.AIService;
   import com.aisupport.service.AIServiceFactory;
   
   AIConfig config = new AIConfig();
   AIService aiService = AIServiceFactory.createService(config);
   String response = aiService.chat("Your message here");
   ```

## Recent Updates

### Version 1.0.0 Features
- **Multi-Engine Support**: Support for both Ollama and VLLM models
- **Configuration Management**: Properties-based configuration with temperature control
- **Connection Testing**: Built-in utilities for testing and diagnosing connection issues
- **Programmatic API**: Clean service interface for easy integration
- **Example Usage**: Basic examples for different use cases
- **Authentication**: API key handling with custom headers
- **LangChain4j Integration**: Integration with LangChain4j for Ollama and OpenAI-compatible APIs

### Key Features
- **AIConnection Utility**: Pre-configured service providers for common use cases
- **Custom HTTP Client**: Custom HTTP client implementation for VLLM connections
- **Service Factory**: Factory pattern for creating appropriate AI services
- **Connection Testing**: Comprehensive connection testing utilities
- **Configuration Classes**: Separate configuration classes for different use cases

## Dependencies

### Core LangChain4j Modules
- **langchain4j-core**: Core AI framework functionality
- **langchain4j-ollama**: Ollama model integration
- **langchain4j-open-ai**: OpenAI-compatible API (VLLM support)

### Additional LangChain4j Modules (Available but not actively used in core functionality)
- **langchain4j-anthropic**: Anthropic Claude integration
- **langchain4j-azure-open-ai**: Azure OpenAI integration
- **langchain4j-easy-rag**: Easy Retrieval-Augmented Generation
- **langchain4j-embeddings-all-minilm-l6-v2**: Embeddings support

### Supporting Libraries
- **OkHttp 4.12.0**: HTTP client for API calls
- **Jackson 2.15.2**: JSON processing
- **SLF4J 2.0.9**: Logging framework
- **JUnit 5.10.0**: Testing framework

### Maven Plugins
- **maven-compiler-plugin 3.11.0**: Java 17 compilation
- **maven-surefire-plugin 3.1.2**: Test execution
- **exec-maven-plugin 3.1.0**: Application execution

## Troubleshooting

### Connection Testing

Before troubleshooting, use the built-in connection tester:

```bash
# Test current configuration
mvn exec:java -Dexec.mainClass="com.aisupport.util.ConnectionTester"

# Test with example usage
mvn exec:java -Dexec.mainClass="com.aisupport.example.ExampleUsage"
```

The connection tester will perform:
- **Basic Connectivity**: Tests if the server is reachable
- **API Endpoint**: Verifies API endpoints are accessible
- **Model Availability**: Checks if the specified model is available
- **Authentication**: Tests API key and authentication headers

### Common Issues

#### Connection Issues
- **Server Unreachable**: Verify your server is running and accessible
- **Wrong URL/Port**: Check the API URL and port in configuration
- **Network Problems**: Check firewall settings and network connectivity
- **DNS Issues**: Ensure server hostname resolves correctly

#### Authentication Issues (403 Forbidden)
1. **API Key Problems**:
   - Verify the API key is correct and not expired
   - Check if the API key has proper permissions
   - Ensure the key format matches server requirements

2. **Header Issues**:
   - The application automatically adds Authorization headers
   - Check if server requires specific header formats
   - Verify X-API-Key vs Authorization header requirements

3. **Server Configuration**:
   - Check if the server requires authentication
   - Verify server allows external connections
   - Check nginx/load balancer configuration

#### Model Issues
- **Model Not Found (404)**: Ensure the model is installed/loaded on the server
- **Model Loading Errors**: Check server logs for model loading issues
- **Compatibility Issues**: Verify model compatibility with server version
- **Resource Constraints**: Check if server has enough resources to load the model

#### Configuration Issues
- **Properties File**: Ensure `ai-config.properties` is in the correct location
- **Property Values**: Check property names and values are correct
- **Engine Type**: Verify engine type is either 'ollama' or 'vllm'
- **Temperature Range**: Temperature values should be between 0.0 and 1.0
- **URL Format**: Ensure URLs end with appropriate paths (/ for Ollama, /v1 for VLLM)

### Debugging Steps

1. **Enable Debug Logging**: Add logging configuration to see detailed connection information
2. **Test with curl**: Use curl to test API endpoints manually
3. **Check Server Logs**: Review server-side logs for error details
4. **Verify Network**: Use ping/telnet to test basic connectivity
5. **Test Different Models**: Try with different model names to isolate issues

### Performance Issues

- **Slow Responses**: Check server resources and model size
- **Timeout Errors**: Increase timeout values in configuration
- **Memory Issues**: Ensure sufficient memory for model loading
- **Concurrent Requests**: Check if server supports multiple concurrent requests

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the MIT License.
