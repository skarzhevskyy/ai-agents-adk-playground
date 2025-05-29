package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Chat interface for communicating with Google AI Studio.
 * Handles the HTTP communication with the AI service.
 */
public class ChatInterface {
    private static final Logger logger = LoggerFactory.getLogger(ChatInterface.class);
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;

    public ChatInterface(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    /**
     * Send a message to the AI and get a response.
     * For demo purposes, this simulates an AI response when no API key is provided.
     * 
     * @param message The message to send
     * @return The AI's response
     * @throws IOException if there's an error communicating with the API
     */
    public String sendMessage(String message) throws IOException {
        logger.info("Sending message to AI: {}", message);
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("No API key provided, returning mock response");
            return generateMockResponse(message);
        }

        try {
            return sendToGoogleAI(message);
        } catch (Exception e) {
            logger.warn("Failed to call Google AI API, falling back to mock response: {}", e.getMessage());
            return generateMockResponse(message);
        }
    }

    private String sendToGoogleAI(String message) throws IOException {
        JsonObject requestBody = new JsonObject();
        JsonObject contents = new JsonObject();
        JsonObject parts = new JsonObject();
        parts.addProperty("text", message);
        contents.add("parts", gson.toJsonTree(new JsonObject[]{parts}));
        requestBody.add("contents", gson.toJsonTree(new JsonObject[]{contents}));

        String url = BASE_URL + "?key=" + apiKey;
        
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
                ))
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            return parseAIResponse(responseBody);
        }
    }

    private String parseAIResponse(String responseBody) {
        try {
            JsonObject response = gson.fromJson(responseBody, JsonObject.class);
            return response.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
        } catch (Exception e) {
            logger.error("Failed to parse AI response: {}", e.getMessage());
            return "Sorry, I couldn't understand the response from the AI service.";
        }
    }

    private String generateMockResponse(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("weather") && lowerMessage.contains("temperature")) {
            return "I can help you check the weather and temperature for any location. What city would you like to know about?";
        } else if (lowerMessage.contains("weather")) {
            return "I can provide weather information for any location. Please specify which city you'd like to know about.";
        } else if (lowerMessage.contains("temperature")) {
            return "I can check the current temperature for you. Which location are you interested in?";
        } else if (lowerMessage.contains("rain")) {
            return "I can check if it's raining in a specific location. Where would you like me to check?";
        } else if (lowerMessage.contains("hello") || lowerMessage.contains("hi")) {
            return "Hello! I'm a weather assistant. I can help you check the weather, temperature, and rain status for any location.";
        } else {
            return "I'm a weather assistant. I can help you with weather information, temperature checks, and rain status for any location. How can I assist you today?";
        }
    }
}