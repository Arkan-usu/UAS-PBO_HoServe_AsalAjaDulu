package com.hoserve.client.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hoserve.dto.response.ApiResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Utility class to perform HTTP requests to the REST backend.
 * Uses Java 11 HttpClient and Jackson ObjectMapper.
 */
public class HttpClientUtil {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Sends a POST request.
     */
    public static <R, T> ApiResponse<T> post(String path, R requestBody, Class<T> responseDataClass) throws Exception {
        String jsonBody = requestBody != null ? objectMapper.writeValueAsString(requestBody) : "";
        
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        addAuthHeader(requestBuilder);

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        return deserializeResponse(response, responseDataClass);
    }

    /**
     * Sends a GET request.
     */
    public static <T> ApiResponse<T> get(String path, Class<T> responseDataClass) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .GET();

        addAuthHeader(requestBuilder);

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        return deserializeResponse(response, responseDataClass);
    }

    /**
     * Sends a GET request expecting a List or collection of responseDataClass.
     * Jackson uses constructParametricType to handle generic List.
     */
    public static <T> ApiResponse<java.util.List<T>> getList(String path, Class<T> elementClass) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .GET();

        addAuthHeader(requestBuilder);

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        
        JavaType listType = objectMapper.getTypeFactory().constructParametricType(java.util.List.class, elementClass);
        JavaType apiResponseType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, listType);
        
        return objectMapper.readValue(response.body(), apiResponseType);
    }

    /**
     * Sends a PUT request.
     */
    public static <R, T> ApiResponse<T> put(String path, R requestBody, Class<T> responseDataClass) throws Exception {
        String jsonBody = requestBody != null ? objectMapper.writeValueAsString(requestBody) : "";

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

        addAuthHeader(requestBuilder);

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        return deserializeResponse(response, responseDataClass);
    }

    /**
     * Sends a DELETE request.
     */
    public static <T> ApiResponse<T> delete(String path, Class<T> responseDataClass) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .DELETE();

        addAuthHeader(requestBuilder);

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        return deserializeResponse(response, responseDataClass);
    }

    private static void addAuthHeader(HttpRequest.Builder builder) {
        String token = ClientSession.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    private static <T> ApiResponse<T> deserializeResponse(HttpResponse<String> response, Class<T> dataClass) throws Exception {
        // If response is a 4xx or 5xx, still try to parse as ApiResponse to extract error message
        String body = response.body();
        if (body == null || body.trim().isEmpty()) {
            ApiResponse<T> error = new ApiResponse<>();
            error.setSuccess(false);
            error.setMessage("Empty server response (HTTP " + response.statusCode() + ")");
            return error;
        }

        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, dataClass);
            return objectMapper.readValue(body, type);
        } catch (Exception e) {
            ApiResponse<T> error = new ApiResponse<>();
            error.setSuccess(false);
            error.setMessage("Failed to parse response: " + e.getMessage() + " (HTTP " + response.statusCode() + "). Raw: " + body);
            return error;
        }
    }
}
