package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A tool for getting weather information using OpenWeatherMap API.
 * This is a demo implementation that can be extended to work with real weather APIs.
 */
public class WeatherTool {
    private static final Logger logger = LoggerFactory.getLogger(WeatherTool.class);
    private final OkHttpClient httpClient;
    private final Gson gson;

    public WeatherTool() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    /**
     * Get weather information for a specific location.
     * For demo purposes, this returns mock data instead of calling a real API.
     * 
     * @param location The location to get weather for
     * @return Weather information as a string
     */
    public String getWeather(String location) {
        logger.info("Getting weather for location: {}", location);
        
        // For demo purposes, return mock weather data
        // In a real implementation, this would call an actual weather API
        return createMockWeatherResponse(location);
    }

    /**
     * Get the current temperature for a location.
     * 
     * @param location The location to get temperature for
     * @return Temperature information
     */
    public String getTemperature(String location) {
        logger.info("Getting temperature for location: {}", location);
        
        // Handle null or empty location
        String safeLocation = (location == null || location.trim().isEmpty()) ? "Unknown Location" : location;
        
        // Mock temperature data
        int temperature = getMockTemperature(location);
        return String.format("The current temperature in %s is %d°C", safeLocation, temperature);
    }

    /**
     * Check if it's raining in a specific location.
     * 
     * @param location The location to check
     * @return Rain status information
     */
    public String isRaining(String location) {
        logger.info("Checking rain status for location: {}", location);
        
        // Handle null or empty location
        String safeLocation = (location == null || location.trim().isEmpty()) ? "Unknown Location" : location;
        
        boolean raining = getMockRainStatus(location);
        return String.format("It is %s raining in %s", raining ? "currently" : "not", safeLocation);
    }

    private String createMockWeatherResponse(String location) {
        // Handle null or empty location
        String safeLocation = (location == null || location.trim().isEmpty()) ? "Unknown Location" : location;
        
        JsonObject weather = new JsonObject();
        weather.addProperty("location", safeLocation);
        weather.addProperty("temperature", getMockTemperature(location));
        weather.addProperty("condition", getMockCondition(location));
        weather.addProperty("humidity", getMockHumidity(location));
        weather.addProperty("raining", getMockRainStatus(location));
        
        return String.format("Weather in %s: %d°C, %s, Humidity: %d%%, %s", 
            safeLocation,
            getMockTemperature(location),
            getMockCondition(location),
            getMockHumidity(location),
            getMockRainStatus(location) ? "Raining" : "Not raining"
        );
    }

    private int getMockTemperature(String location) {
        // Handle null or empty location
        if (location == null || location.trim().isEmpty()) {
            return 20; // Default temperature
        }
        // Simple hash-based mock temperature (15-30°C range)
        return 15 + Math.abs(location.hashCode() % 16);
    }

    private String getMockCondition(String location) {
        if (location == null || location.trim().isEmpty()) {
            return "Cloudy"; // Default condition
        }
        String[] conditions = {"Sunny", "Cloudy", "Partly Cloudy", "Overcast"};
        return conditions[Math.abs(location.hashCode() % conditions.length)];
    }

    private int getMockHumidity(String location) {
        if (location == null || location.trim().isEmpty()) {
            return 50; // Default humidity
        }
        // Mock humidity (40-80% range)
        return 40 + Math.abs(location.hashCode() % 41);
    }

    private boolean getMockRainStatus(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false; // Default not raining
        }
        // Mock rain status based on location hash
        return location.hashCode() % 3 == 0;
    }
}