package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for MultiToolAgent class.
 * Tests the agent's tool selection and query processing logic.
 */
class MultiToolAgentTest {
    
    private MultiToolAgent agent;
    
    @BeforeEach
    void setUp() {
        // Create agent with null API key to test mock responses
        agent = new MultiToolAgent(null);
    }
    
    @Test
    @DisplayName("Should route weather queries to weather tool")
    void shouldRouteWeatherQueriesToWeatherTool() {
        String response = agent.processQuery("What's the weather in London?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("london")
            .containsIgnoringCase("weather")
            .contains("°C");
    }
    
    @Test
    @DisplayName("Should route temperature queries to temperature tool")
    void shouldRouteTemperatureQueriesToTemperatureTool() {
        String response = agent.processQuery("What's the temperature in Berlin?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("berlin")
            .containsIgnoringCase("temperature")
            .contains("°C");
    }
    
    @Test
    @DisplayName("Should route rain queries to rain tool")
    void shouldRouteRainQueriesToRainTool() {
        String response = agent.processQuery("Is it raining in Sydney?");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("sydney")
            .containsAnyOf("raining", "not raining");
    }
    
    @Test
    @DisplayName("Should handle queries without location by asking for clarification")
    void shouldHandleQueriesWithoutLocationByAskingForClarification() {
        String weatherResponse = agent.processQuery("What's the weather?");
        String tempResponse = agent.processQuery("What's the temperature?");
        String rainResponse = agent.processQuery("Is it raining?");
        
        assertThat(weatherResponse)
            .containsAnyOf("specify", "location", "which", "where");
        
        assertThat(tempResponse)
            .containsAnyOf("specify", "location", "which", "where");
        
        assertThat(rainResponse)
            .containsAnyOf("specify", "location", "which", "where");
    }
    
    @Test
    @DisplayName("Should extract location from various query formats")
    void shouldExtractLocationFromVariousQueryFormats() {
        String[] queries = {
            "Weather in Madrid",
            "What's the weather for Rome?",
            "Tell me the weather at Barcelona",
            "Temperature in Vienna",
            "Is it raining in Dublin?"
        };
        
        String[] expectedLocations = {"Madrid", "Rome", "Barcelona", "Vienna", "Dublin"};
        
        for (int i = 0; i < queries.length; i++) {
            String response = agent.processQuery(queries[i]);
            assertThat(response)
                .isNotNull()
                .containsIgnoringCase(expectedLocations[i]);
        }
    }
    
    @Test
    @DisplayName("Should fall back to chat interface for non-tool queries")
    void shouldFallBackToChatInterfaceForNonToolQueries() {
        String response = agent.processQuery("Hello, how are you?");
        
        assertThat(response)
            .isNotNull()
            .containsAnyOf("hello", "weather", "assistant", "help");
    }
    
    @Test
    @DisplayName("Should provide available tools information")
    void shouldProvideAvailableToolsInformation() {
        String toolsInfo = agent.getAvailableTools();
        
        assertThat(toolsInfo)
            .isNotNull()
            .containsIgnoringCase("get_weather")
            .containsIgnoringCase("get_temperature")
            .containsIgnoringCase("check_rain")
            .containsIgnoringCase("example");
    }
    
    @Test
    @DisplayName("Should handle case insensitive queries")
    void shouldHandleCaseInsensitiveQueries() {
        String upperCase = agent.processQuery("WEATHER IN LONDON");
        String lowerCase = agent.processQuery("weather in london");
        String mixedCase = agent.processQuery("Weather In London");
        
        assertThat(upperCase).containsIgnoringCase("london");
        assertThat(lowerCase).containsIgnoringCase("london");
        assertThat(mixedCase).containsIgnoringCase("london");
    }
    
    @Test
    @DisplayName("Should differentiate between weather and temperature queries")
    void shouldDifferentiateBetweenWeatherAndTemperatureQueries() {
        String weatherResponse = agent.processQuery("What's the weather in Paris?");
        String temperatureResponse = agent.processQuery("What's the temperature in Paris?");
        
        // Weather query should include comprehensive info
        assertThat(weatherResponse)
            .containsIgnoringCase("paris")
            .containsIgnoringCase("weather")
            .containsIgnoringCase("humidity");
        
        // Temperature query should be more focused
        assertThat(temperatureResponse)
            .containsIgnoringCase("paris")
            .containsIgnoringCase("temperature")
            .contains("°C");
    }
    
    @Test
    @DisplayName("Should handle queries with multiple weather terms")
    void shouldHandleQueriesWithMultipleWeatherTerms() {
        String response = agent.processQuery("What's the weather and temperature in London?");
        
        // Should default to comprehensive weather info when multiple terms present
        assertThat(response)
            .containsIgnoringCase("london")
            .containsIgnoringCase("weather");
    }
    
    @Test
    @DisplayName("Should handle empty and null queries gracefully")
    void shouldHandleEmptyAndNullQueriesGracefully() {
        String emptyResponse = agent.processQuery("");
        String nullResponse = agent.processQuery(null);
        
        assertThat(emptyResponse).isNotNull().isNotEmpty();
        assertThat(nullResponse).isNotNull().isNotEmpty();
    }
    
    @Test
    @DisplayName("Should ignore common words when extracting location")
    void shouldIgnoreCommonWordsWhenExtractingLocation() {
        String response1 = agent.processQuery("What is the weather today?");
        String response2 = agent.processQuery("Does it rain now?");
        
        // These should ask for location since "today" and "now" are filtered out
        assertThat(response1).containsAnyOf("specify", "location", "which", "where");
        assertThat(response2).containsAnyOf("specify", "location", "which", "where");
    }
    
    @Test
    @DisplayName("Should handle complex sentence structures")
    void shouldHandleComplexSentenceStructures() {
        String[] complexQueries = {
            "Can you please tell me what the weather is like in Stockholm today?",
            "I would like to know the current temperature for Oslo",
            "Could you check if it's currently raining in Copenhagen?"
        };
        
        String[] expectedLocations = {"Stockholm", "Oslo", "Copenhagen"};
        
        for (int i = 0; i < complexQueries.length; i++) {
            String response = agent.processQuery(complexQueries[i]);
            assertThat(response)
                .isNotNull()
                .containsIgnoringCase(expectedLocations[i]);
        }
    }
}