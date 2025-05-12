package com.taskmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.model.User;

@Service
public class AuthService {

    private final String BASE_URL = "http://localhost:8083";  // Base URL for the backend API

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to register a user
    public void registerUser(User user) {
        String url = BASE_URL + "/register"; // Append the register endpoint to the base URL
        try {
            restTemplate.postForObject(url, user, User.class); // Sending POST request to the backend API
        } catch (Exception e) {
            // Exception handling: Providing a custom error message
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
}
