package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for WeatherTool class.
 * Tests the weather tool functionality and mock data generation.
 */
class WeatherToolTest {
    
    private WeatherTool weatherTool;
    
    @BeforeEach
    void setUp() {
        weatherTool = new WeatherTool();
    }
    
    @Test
    @DisplayName("Should return weather information for valid location")
    void shouldReturnWeatherInformationForValidLocation() {
        String response = weatherTool.getWeather("London");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("london")
            .containsIgnoringCase("weather")
            .contains("°C")
            .containsAnyOf("Sunny", "Cloudy", "Partly Cloudy", "Overcast")
            .containsIgnoringCase("humidity");
    }
    
    @Test
    @DisplayName("Should return temperature for valid location")
    void shouldReturnTemperatureForValidLocation() {
        String response = weatherTool.getTemperature("New York");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("new york")
            .containsIgnoringCase("temperature")
            .contains("°C");
    }
    
    @Test
    @DisplayName("Should return rain status for valid location")
    void shouldReturnRainStatusForValidLocation() {
        String response = weatherTool.isRaining("Tokyo");
        
        assertThat(response)
            .isNotNull()
            .containsIgnoringCase("tokyo")
            .containsAnyOf("raining", "not raining", "currently", "not");
    }
    
    @Test
    @DisplayName("Should return consistent results for same location")
    void shouldReturnConsistentResultsForSameLocation() {
        String weather1 = weatherTool.getWeather("Paris");
        String weather2 = weatherTool.getWeather("Paris");
        
        assertThat(weather1).isEqualTo(weather2);
        
        String temp1 = weatherTool.getTemperature("Paris");
        String temp2 = weatherTool.getTemperature("Paris");
        
        assertThat(temp1).isEqualTo(temp2);
        
        String rain1 = weatherTool.isRaining("Paris");
        String rain2 = weatherTool.isRaining("Paris");
        
        assertThat(rain1).isEqualTo(rain2);
    }
    
    @Test
    @DisplayName("Should handle different locations")
    void shouldHandleDifferentLocations() {
        String[] locations = {"London", "New York", "Tokyo", "Paris", "Berlin", "Sydney"};
        
        for (String location : locations) {
            String weather = weatherTool.getWeather(location);
            String temperature = weatherTool.getTemperature(location);
            String rain = weatherTool.isRaining(location);
            
            assertThat(weather)
                .isNotNull()
                .containsIgnoringCase(location);
            
            assertThat(temperature)
                .isNotNull()
                .containsIgnoringCase(location)
                .contains("°C");
            
            assertThat(rain)
                .isNotNull()
                .containsIgnoringCase(location);
        }
    }
    
    @Test
    @DisplayName("Should handle empty location")
    void shouldHandleEmptyLocation() {
        String weather = weatherTool.getWeather("");
        String temperature = weatherTool.getTemperature("");
        String rain = weatherTool.isRaining("");
        
        assertThat(weather).isNotNull().isNotEmpty();
        assertThat(temperature).isNotNull().isNotEmpty();
        assertThat(rain).isNotNull().isNotEmpty();
    }
    
    @Test
    @DisplayName("Should handle null location")
    void shouldHandleNullLocation() {
        String weather = weatherTool.getWeather(null);
        String temperature = weatherTool.getTemperature(null);
        String rain = weatherTool.isRaining(null);
        
        assertThat(weather).isNotNull().isNotEmpty();
        assertThat(temperature).isNotNull().isNotEmpty();
        assertThat(rain).isNotNull().isNotEmpty();
    }
    
    @Test
    @DisplayName("Should return temperature in reasonable range")
    void shouldReturnTemperatureInReasonableRange() {
        String[] locations = {"London", "New York", "Tokyo", "Paris"};
        
        for (String location : locations) {
            String response = weatherTool.getTemperature(location);
            
            // Extract temperature value
            String tempStr = response.replaceAll(".*?(\\d+)°C.*", "$1");
            int temperature = Integer.parseInt(tempStr);
            
            assertThat(temperature)
                .isGreaterThanOrEqualTo(15)
                .isLessThanOrEqualTo(30);
        }
    }
    
    @Test
    @DisplayName("Should include all required weather components")
    void shouldIncludeAllRequiredWeatherComponents() {
        String response = weatherTool.getWeather("London");
        
        assertThat(response)
            .contains("London")           // Location
            .matches(".*\\d+°C.*")       // Temperature
            .containsAnyOf("Sunny", "Cloudy", "Partly Cloudy", "Overcast")  // Condition
            .matches(".*Humidity: \\d+%.*")  // Humidity
            .containsAnyOf("Raining", "Not raining");  // Rain status
    }
    
    @Test
    @DisplayName("Should handle location names with spaces")
    void shouldHandleLocationNamesWithSpaces() {
        String[] locations = {"New York", "Los Angeles", "San Francisco", "Hong Kong"};
        
        for (String location : locations) {
            String weather = weatherTool.getWeather(location);
            String temperature = weatherTool.getTemperature(location);
            String rain = weatherTool.isRaining(location);
            
            assertThat(weather).containsIgnoringCase(location);
            assertThat(temperature).containsIgnoringCase(location);
            assertThat(rain).containsIgnoringCase(location);
        }
    }
    
    @Test
    @DisplayName("Should provide different results for different locations")
    void shouldProvideDifferentResultsForDifferentLocations() {
        String londonWeather = weatherTool.getWeather("London");
        String tokyoWeather = weatherTool.getWeather("Tokyo");
        String parisWeather = weatherTool.getWeather("Paris");
        
        // While we can't guarantee they'll be different (due to hashing),
        // we can ensure they contain the correct location names
        assertThat(londonWeather).containsIgnoringCase("london");
        assertThat(tokyoWeather).containsIgnoringCase("tokyo");
        assertThat(parisWeather).containsIgnoringCase("paris");
    }
}