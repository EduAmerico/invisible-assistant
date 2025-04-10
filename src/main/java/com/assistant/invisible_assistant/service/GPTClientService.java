package com.assistant.invisible_assistant.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GPTClientService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    public String analyzeImage(File imageFile) {
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            JSONObject imageContent = new JSONObject()
                .put("type", "image_url")
                .put("image_url", new JSONObject().put("url", "data:image/png;base64," + base64Image));

            JSONObject promptContent = new JSONObject()
                .put("type", "text")
                .put("text", "Analyze this image and give me the code to solve the problem. Format your response with markdown for code blocks.");

            JSONArray content = new JSONArray().put(imageContent).put(promptContent);

            JSONArray messages = new JSONArray()
                .put(new JSONObject()
                    .put("role", "user")
                    .put("content", content));

            JSONObject requestBody = new JSONObject()
                .put("model", "gpt-4-vision-preview")
                .put("messages", messages)
                .put("max_tokens", 1000);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Se tivermos uma resposta de sucesso
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                System.err.println("Erro API OpenAI: " + response.statusCode() + " - " + response.body());
                return "{\"error\":\"Erro API OpenAI: " + response.statusCode() + "\"}";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
        }
    }
}