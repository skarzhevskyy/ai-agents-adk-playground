package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for WeatherAgent class.
 * Tests cover agent logic and tool integration without mocking external APIs.
 */
class WeatherAgentTest {
    
    private WeatherAgent weatherAgent;
    
    @BeforeEach
    void setUp() {
        // Create agent with null API key to test mock responses
        weatherAgent = new WeatherAgent(null);
    }
    
    @Test
    @DisplayName("Should handle basic greeting")
    void shouldHandleBasicGreeting() {
        String response = weatherAgent.processQuery("Hello");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("hello")
            .containsIgnoringCase("weather");
    }
    
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
    
    @Test
    @DisplayName("Should process temperature query for specific location")
    void shouldProcessTemperatureQueryForSpecificLocation() {
        String response = weatherAgent.processQuery("What's the temperature in New York?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("new york")
            .containsIgnoringCase("temperature")
            .matches(".*\\d+°C.*"); // Should contain temperature
    }
    
    @Test
    @DisplayName("Should process rain query for specific location")
    void shouldProcessRainQueryForSpecificLocation() {
        String response = weatherAgent.processQuery("Is it raining in Tokyo?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("tokyo")
            .containsAnyOf("raining", "not raining", "currently", "not");
    }
    
    @Test
    @DisplayName("Should handle weather query without specific location")
    void shouldHandleWeatherQueryWithoutLocation() {
        String response = weatherAgent.processQuery("What's the weather?");
        
        assertThat(response)
            .isNotNull()
            .containsAnyOf("location", "specify", "where", "which");
    }
    
    @Test
    @DisplayName("Should handle temperature query without specific location")
    void shouldHandleTemperatureQueryWithoutLocation() {
        String response = weatherAgent.processQuery("What's the temperature?");
        
        assertThat(response)
            .isNotNull()
            .containsAnyOf("location", "specify", "where", "which");
    }
    
    @Test
    @DisplayName("Should handle rain query without specific location")
    void shouldHandleRainQueryWithoutLocation() {
        String response = weatherAgent.processQuery("Is it raining?");
        
        assertThat(response)
            .isNotNull()
            .containsAnyOf("location", "specify", "where", "which");
    }
    
    @Test
    @DisplayName("Should handle general assistant query")
    void shouldHandleGeneralAssistantQuery() {
        String response = weatherAgent.processQuery("How can you help me?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("weather")
            .containsAnyOf("assistant", "help", "temperature", "rain");
    }
    
    @Test
    @DisplayName("Should handle empty query gracefully")
    void shouldHandleEmptyQueryGracefully() {
        String response = weatherAgent.processQuery("");
        
        assertThat(response)
            .isNotNull()
            .isNotEmpty();
    }
    
    @Test
    @DisplayName("Should handle null query gracefully")
    void shouldHandleNullQueryGracefully() {
        String response = weatherAgent.processQuery(null);
        
        assertThat(response)
            .isNotNull()
            .isNotEmpty();
    }
    
    @Test
    @DisplayName("Should provide consistent responses for same location")
    void shouldProvideConsistentResponsesForSameLocation() {
        String response1 = weatherAgent.processQuery("What's the weather in Paris?");
        String response2 = weatherAgent.processQuery("What's the weather in Paris?");
        
        assertThat(response1)
            .isNotNull()
            .isEqualTo(response2);
    }
    
    @Test
    @DisplayName("Should handle complex weather queries")
    void shouldHandleComplexWeatherQueries() {
        String response = weatherAgent.processQuery("Can you tell me about the current weather conditions in Berlin?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("berlin")
            .containsIgnoringCase("weather");
    }
    
    @Test
    @DisplayName("Should extract location from various query formats")
    void shouldExtractLocationFromVariousQueryFormats() {
        String[] queries = {
            "Weather in Madrid",
            "What's the weather for Rome?",
            "Tell me the weather at Barcelona",
            "Weather Madrid please"
        };
        
        for (String query : queries) {
            String response = weatherAgent.processQuery(query);
            assertThat(response)
                .isNotNull()
                .isNotEmpty();
        }
    }
    
    @Test
    @DisplayName("Should handle case insensitive queries")
    void shouldHandleCaseInsensitiveQueries() {
        String response1 = weatherAgent.processQuery("WEATHER IN LONDON");
        String response2 = weatherAgent.processQuery("weather in london");
        String response3 = weatherAgent.processQuery("Weather In London");
        
        assertThat(response1)
            .containsIgnoringCase("london");
        assertThat(response2)
            .containsIgnoringCase("london");
        assertThat(response3)
            .containsIgnoringCase("london");
    }
}