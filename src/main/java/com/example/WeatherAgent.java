package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Main WeatherAgent class that demonstrates the use of Google's Agent Development Kit pattern
 * for building AI agents with tools. This agent can handle weather-related queries using
 * various weather tools.
 */
public class WeatherAgent {
    private static final Logger logger = LoggerFactory.getLogger(WeatherAgent.class);
    private static final String API_KEY_ENV_VAR = "GOOGLE_AISTUDIO_API_KEY";
    
    private final MultiToolAgent agent;

    public WeatherAgent(String apiKey) {
        this.agent = new MultiToolAgent(apiKey);
    }

    /**
     * Process a single query and return the response.
     * 
     * @param query The user's query
     * @return The agent's response
     */
    public String processQuery(String query) {
        return agent.processQuery(query);
    }

    /**
     * Start an interactive chat session with the weather agent.
     */
    public void startInteractiveSession() {
        logger.info("Starting interactive weather agent session");
        System.out.println("=== Weather Agent Interactive Session ===");
        System.out.println("I'm your weather assistant! Ask me about weather, temperature, or rain in any location.");
        System.out.println("Type 'help' for available commands, or 'quit' to exit.\n");

        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                System.out.println("Weather Agent: Goodbye! Have a great day!");
                break;
            }
            
            if (input.equalsIgnoreCase("help")) {
                System.out.println("Weather Agent: " + agent.getAvailableTools());
                continue;
            }
            
            try {
                String response = processQuery(input);
                System.out.println("Weather Agent: " + response);
            } catch (Exception e) {
                logger.error("Error processing query: {}", e.getMessage());
                System.out.println("Weather Agent: I apologize, but I encountered an error. Please try again.");
            }
            
            System.out.println(); // Add a blank line for readability
        }
        
        scanner.close();
    }

    /**
     * Validate that the Google AI Studio API key is available.
     * 
     * @return The API key if available, null if not set
     */
    private static String validateApiKey() {
        String apiKey = System.getenv(API_KEY_ENV_VAR);
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("Environment variable {} is not set or empty. Using demo mode with mock responses.", API_KEY_ENV_VAR);
            System.out.println("WARNING: " + API_KEY_ENV_VAR + " environment variable is not set.");
            System.out.println("Running in demo mode with simulated AI responses.");
            System.out.println("To use real Google AI Studio, set the environment variable with your API key.\n");
            return null;
        }
        
        logger.info("Google AI Studio API key found, using real AI responses");
        return apiKey;
    }

    /**
     * Run some example queries to demonstrate the agent's capabilities.
     * 
     * @param weatherAgent The weather agent instance
     */
    private static void runExamples(WeatherAgent weatherAgent) {
        System.out.println("=== Weather Agent Demo Examples ===\n");
        
        String[] exampleQueries = {
            "Hello! What can you help me with?",
            "What's the weather in London?",
            "What's the temperature in New York?",
            "Is it raining in Tokyo?",
            "Tell me about the weather in Paris"
        };
        
        for (String query : exampleQueries) {
            System.out.println("Example Query: " + query);
            String response = weatherAgent.processQuery(query);
            System.out.println("Agent Response: " + response);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        logger.info("Starting WeatherAgent application");
        
        // Validate API key
        String apiKey = validateApiKey();
        
        // Create weather agent
        WeatherAgent weatherAgent = new WeatherAgent(apiKey);
        
        // Check command line arguments
        if (args.length > 0) {
            if (args[0].equals("--examples") || args[0].equals("-e")) {
                runExamples(weatherAgent);
                return;
            } else if (args[0].equals("--help") || args[0].equals("-h")) {
                System.out.println("Weather Agent - Google ADK Demo");
                System.out.println("Usage:");
                System.out.println("  java -jar weather-agent.jar          Start interactive session");
                System.out.println("  java -jar weather-agent.jar -e       Run example queries");
                System.out.println("  java -jar weather-agent.jar -h       Show this help");
                System.out.println();
                System.out.println("Environment Variables:");
                System.out.println("  " + API_KEY_ENV_VAR + "  Google AI Studio API key (optional for demo)");
                return;
            } else {
                // Treat the argument as a single query
                String query = String.join(" ", args);
                String response = weatherAgent.processQuery(query);
                System.out.println(response);
                return;
            }
        }
        
        // Start interactive session
        weatherAgent.startInteractiveSession();
    }
}