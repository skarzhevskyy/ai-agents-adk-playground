# AI Agents ADK Playground

A Java-based demo project showcasing Google's Agent Development Kit (ADK) pattern for building AI agents with tools. This project demonstrates how to create intelligent agents that can use various tools to respond to user queries, specifically focusing on weather-related functionality.

## Features

- **Multi-Tool Agent**: Demonstrates the ADK pattern with multiple specialized tools
- **Weather Tools**: Get weather information, temperature, and rain status for any location
- **Chat Interface**: Communicates with Google AI Studio (with fallback to mock responses)
- **Interactive Mode**: Command-line interface for real-time interaction
- **Comprehensive Testing**: Unit tests with >80% code coverage using JUnit and AssertJ

## Project Structure

```
src/
├── main/java/com/example/
│   ├── WeatherAgent.java      # Main class with CLI interface
│   ├── MultiToolAgent.java    # Core agent implementing ADK pattern
│   ├── WeatherTool.java       # Weather information tool
│   └── ChatInterface.java     # AI communication interface
└── test/java/com/example/
    ├── WeatherAgentTest.java
    ├── MultiToolAgentTest.java
    └── WeatherToolTest.java
```

## Prerequisites

- **Java 11 or higher**
- **Gradle 8.0 or higher**
- **Google AI Studio API Key** (optional - project works with mock data)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/skarzhevskyy/ai-agents-adk-playground.git
cd ai-agents-adk-playground
```

### 2. Set Up Environment Variables (Optional)

To use real Google AI Studio responses, set your API key:

```bash
export GOOGLE_AISTUDIO_API_KEY="your-api-key-here"
```

**Note**: If the API key is not set, the application will run in demo mode with simulated AI responses.

### 3. Build the Project

```bash
./gradlew build
```

### 4. Run the Application

#### Interactive Mode (Default)

```bash
./gradlew run
```

#### Run Example Queries

```bash
./gradlew run --args="--examples"
```

#### Single Query

```bash
./gradlew run --args="What's the weather in London?"
```

#### Help

```bash
./gradlew run --args="--help"
```

## Running the Demo

### Interactive Session

Start an interactive session where you can chat with the weather agent:

```bash
$ ./gradlew run

=== Weather Agent Interactive Session ===
I'm your weather assistant! Ask me about weather, temperature, or rain in any location.
Type 'help' for available commands, or 'quit' to exit.

You: What's the weather in London?
Weather Agent: Weather in London: 23°C, Sunny, Humidity: 40%, Not raining

You: What's the temperature in New York?
Weather Agent: The current temperature in New York is 30°C

You: Is it raining in Tokyo?
Weather Agent: It is currently raining in Tokyo

You: quit
Weather Agent: Goodbye! Have a great day!
```

### Example Output

```
=== Weather Agent Demo Examples ===

Example Query: Hello! What can you help me with?
Agent Response: Hello! I'm a weather assistant. I can help you check the weather, temperature, and rain status for any location.

Example Query: What's the weather in London?
Agent Response: Weather in London: 23°C, Sunny, Humidity: 40%, Not raining

Example Query: What's the temperature in New York?
Agent Response: The current temperature in New York is 30°C

Example Query: Is it raining in Tokyo?
Agent Response: It is currently raining in Tokyo

Example Query: Tell me about the weather in Paris
Agent Response: Weather in Paris: 26°C, Overcast, Humidity: 64%, Not raining
```

## Example Prompts

Here are some sample queries you can try:

### Weather Queries
- "What's the weather in London?"
- "Tell me about the weather in Paris"
- "Can you check the weather for Berlin?"
- "Weather in Madrid please"

### Temperature Queries
- "What's the temperature in New York?"
- "Current temperature for Tokyo"
- "How hot is it in Rome?"

### Rain Queries
- "Is it raining in Seattle?"
- "Does it rain in London today?"
- "Check rain status for Mumbai"

### General Queries
- "Hello, what can you help me with?"
- "How can you assist me?"
- "What tools do you have?"

### Expected AI Responses

The agent intelligently routes queries to appropriate tools:

1. **Weather tool**: Provides comprehensive weather information including temperature, conditions, humidity, and rain status
2. **Temperature tool**: Focuses specifically on current temperature
3. **Rain tool**: Checks current precipitation status
4. **Chat interface**: Handles general conversation and clarification requests

## Testing

### Run All Tests

```bash
./gradlew test
```

### Run Specific Test Class

```bash
./gradlew test --tests WeatherAgentTest
./gradlew test --tests WeatherToolTest
./gradlew test --tests MultiToolAgentTest
```

### Test Coverage

The project includes comprehensive tests covering:

- **Agent Logic**: Query processing, tool selection, and response generation
- **Tool Functionality**: Weather data retrieval and formatting
- **Error Handling**: Null inputs, invalid queries, and API failures
- **Edge Cases**: Empty locations, malformed requests, and complex queries

Tests achieve >80% code coverage and use AssertJ for fluent assertions.

### Test Examples

```java
@Test
@DisplayName("Should process weather query for specific location")
void shouldProcessWeatherQueryForSpecificLocation() {
    String response = weatherAgent.processQuery("What's the weather in London?");
    
    assertThat(response)
        .isNotNull()
        .containsIgnoringCase("london")
        .containsIgnoringCase("weather")
        .matches(".*\\d+°C.*"); // Should contain temperature
}
```

## Architecture

### Agent Development Kit Pattern

This project follows Google's ADK pattern with:

1. **Agent**: Main orchestrator that processes queries and selects tools
2. **Tools**: Specialized functions for specific tasks (weather, temperature, rain)
3. **Chat Interface**: Communication layer with AI services
4. **Query Processing**: Natural language understanding for tool selection

### Key Components

- **MultiToolAgent**: Core agent implementing the ADK pattern
- **WeatherTool**: Domain-specific tool for weather operations
- **ChatInterface**: Abstraction for AI service communication
- **WeatherAgent**: Main application with CLI interface

## Configuration

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `GOOGLE_AISTUDIO_API_KEY` | Google AI Studio API key | No (falls back to mock responses) |

### Mock Data

When running without an API key, the application uses deterministic mock data:
- Temperature range: 15-30°C
- Weather conditions: Sunny, Cloudy, Partly Cloudy, Overcast
- Humidity range: 40-80%
- Rain status: Based on location hash

## Development

### Adding New Tools

1. Create a new tool class (e.g., `HumidityTool.java`)
2. Register it in `MultiToolAgent.initializeTools()`
3. Add query patterns in `MultiToolAgent.tryTools()`
4. Create corresponding tests

### Extending Query Processing

The agent uses regex patterns to extract locations and intents:

```java
Pattern[] patterns = {
    Pattern.compile("weather\\s+in\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
    Pattern.compile("temperature\\s+in\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
    // Add new patterns here
};
```

## Troubleshooting

### Common Issues

1. **Build Failures**: Ensure Java 11+ and Gradle 8.0+ are installed
2. **API Errors**: Check `GOOGLE_AISTUDIO_API_KEY` environment variable
3. **Test Failures**: Run `./gradlew clean test` to rebuild and retest

### Logging

The application uses SLF4J for logging. Set log level in `src/main/resources/simplelogger.properties`:

```properties
org.slf4j.simpleLogger.defaultLogLevel=INFO
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass: `./gradlew test`
5. Submit a pull request

## License

This project is licensed under the Apache License 2.0. See [LICENSE](LICENSE) for details.

## Acknowledgments

- Based on Google's Agent Development Kit (ADK) patterns
- Inspired by the [ADK Quickstart](https://google.github.io/adk-docs/get-started/quickstart/) guide