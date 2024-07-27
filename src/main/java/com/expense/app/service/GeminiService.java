package com.expense.app.service;

import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.Recommendation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ExpenseService expenseService;
    private final ObjectMapper objectMapper;

    @Autowired
    public GeminiService(RestTemplate restTemplate, ExpenseService expenseService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.expenseService = expenseService;
        this.objectMapper = objectMapper;
    }

    public List<Recommendation> getRecommendations(List<ExpenseDto> expenses) throws IOException {
        String expensesJson = convertExpensesToJson(expenses);
        String prompt = String.format("{\"contents\" : [{\"parts\": [{\"text\": \"%s Give me some recommendations to the user for this list of expenses on how to spend less. and format it and give as json\"}]}]}", escapeJson(expensesJson));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBTdi8WNPiPzho6B2hucT4qxxz6KCHvrKA"))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(prompt))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response != null) {
            return parseRecommendations(response.body());
        }

        return new ArrayList<>();
    }


    private String convertExpensesToJson(List<ExpenseDto> expenses) throws JsonProcessingException {
        return objectMapper.writeValueAsString(expenses);
    }

    private String escapeJson(String json) {
        return json.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private List<Recommendation> parseRecommendations(String responseBody) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode candidatesNode = rootNode.path("candidates");
        List<Recommendation> recommendations = new ArrayList<>();

        if (candidatesNode.isArray()) {
            for (JsonNode candidateNode : candidatesNode) {
                JsonNode contentNode = candidateNode.path("content");
                JsonNode partsNode = contentNode.path("parts");
                for (JsonNode partNode : partsNode) {
                    String text = partNode.path("text").asText();
                    if (text.contains("```json")) {
                        String jsonPart = text.substring(text.indexOf("{"), text.lastIndexOf("}") + 1);
                        JsonNode recommendationsNode = objectMapper.readTree(jsonPart).path("recommendations");
                        for (JsonNode recommendationNode : recommendationsNode) {
                            String category = recommendationNode.path("category").asText();
                            String suggestion = recommendationNode.path("suggestion").asText();
                            String example = recommendationNode.path("example").asText();
                            recommendations.add(new Recommendation(category, suggestion, example));
                        }
                    }
                }
            }
        }

        return recommendations;
    }
}
