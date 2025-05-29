package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A multi-tool agent that can use various tools to respond to user queries.
 * This implementation is based on the Google ADK Quickstart pattern.
 */
public class MultiToolAgent {
    private static final Logger logger = LoggerFactory.getLogger(MultiToolAgent.class);
    
    private final ChatInterface chatInterface;
    private final WeatherTool weatherTool;
    private final Map<String, Function<String, String>> tools;

    public MultiToolAgent(String apiKey) {
        this.chatInterface = new ChatInterface(apiKey);
        this.weatherTool = new WeatherTool();
        this.tools = new HashMap<>();
        
        initializeTools();
    }

    private void initializeTools() {
        // Register available tools
        tools.put("get_weather", this::handleWeatherQuery);
        tools.put("get_temperature", this::handleTemperatureQuery);
        tools.put("check_rain", this::handleRainQuery);
    }

    /**
     * Process a user query by determining which tools to use and returning a response.
     * 
     * @param userQuery The user's input query
     * @return The agent's response
     */
    public String processQuery(String userQuery) {
        logger.info("Processing query: {}", userQuery);
        
        try {
            // First, try to handle with specific tools
            String toolResponse = tryTools(userQuery);
            if (toolResponse != null) {
                return toolResponse;
            }

            // If no specific tool matches, use the chat interface
            return chatInterface.sendMessage(userQuery);
            
        } catch (Exception e) {
            logger.error("Error processing query: {}", e.getMessage());
            return "I apologize, but I encountered an error while processing your request. Please try again.";
        }
    }

    private String tryTools(String query) {
        String lowerQuery = query.toLowerCase();
        
        // Extract location from query
        String location = extractLocation(query);
        
        // Prioritize weather if both weather and temperature are mentioned
        if (lowerQuery.contains("weather") && lowerQuery.contains("temperature")) {
            return tools.get("get_weather").apply(location != null ? location : "unknown location");
        }
        
        if (lowerQuery.contains("weather") && !lowerQuery.contains("temperature") && !lowerQuery.contains("rain")) {
            return tools.get("get_weather").apply(location != null ? location : "unknown location");
        }
        
        if (lowerQuery.contains("temperature") && !lowerQuery.contains("weather")) {
            return tools.get("get_temperature").apply(location != null ? location : "unknown location");
        }
        
        if (lowerQuery.contains("rain") || lowerQuery.contains("raining")) {
            return tools.get("check_rain").apply(location != null ? location : "unknown location");
        }
        
        return null;
    }

    private String extractLocation(String query) {
        // More specific location extraction patterns
        Pattern[] patterns = {
            Pattern.compile("weather\\s+in\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("temperature\\s+in\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("raining\\s+in\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("rain\\s+in\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(?:weather|temperature|rain)\\s+(?:for|at)\\s+(\\w+(?:\\s+\\w+)?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bin\\s+(\\w+(?:\\s+\\w+)?)(?=\\s*\\?*$)", Pattern.CASE_INSENSITIVE)
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(query);
            if (matcher.find()) {
                String location = matcher.group(1).trim();
                // Filter out common words that aren't locations
                if (!isCommonWord(location.toLowerCase()) && location.length() > 2) {
                    return location;
                }
            }
        }
        
        return null;
    }

    private boolean isCommonWord(String word) {
        String[] commonWords = {"the", "is", "it", "today", "now", "current", "like", "does", "will", "are", "and", "or"};
        for (String commonWord : commonWords) {
            if (word.equals(commonWord)) {
                return true;
            }
        }
        return false;
    }

    private String handleWeatherQuery(String location) {
        if (location == null || location.equals("unknown location")) {
            return "I'd be happy to check the weather for you! Please specify which location you're interested in.";
        }
        return weatherTool.getWeather(location);
    }

    private String handleTemperatureQuery(String location) {
        if (location == null || location.equals("unknown location")) {
            return "I can check the temperature for you! Please let me know which location you're interested in.";
        }
        return weatherTool.getTemperature(location);
    }

    private String handleRainQuery(String location) {
        if (location == null || location.equals("unknown location")) {
            return "I can check if it's raining for you! Please specify which location you'd like me to check.";
        }
        return weatherTool.isRaining(location);
    }

    /**
     * Get information about the available tools.
     * 
     * @return Description of available tools
     */
    public String getAvailableTools() {
        return "Available tools:\n" +
               "- get_weather: Get comprehensive weather information for a location\n" +
               "- get_temperature: Get current temperature for a location\n" +
               "- check_rain: Check if it's currently raining in a location\n" +
               "\nExample queries:\n" +
               "- 'What's the weather in London?'\n" +
               "- 'What's the temperature in New York?'\n" +
               "- 'Is it raining in Tokyo?'";
    }
}