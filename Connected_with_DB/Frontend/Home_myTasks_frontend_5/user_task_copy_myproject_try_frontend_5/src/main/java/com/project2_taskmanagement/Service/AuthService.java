package com.project2_taskmanagement.Service;

import com.project2_taskmanagement.Model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${backend.api.url:http://localhost:8083/api}")
    private String backendApiUrl;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void registerUser(User user) {
        String url = backendApiUrl + "/auth/register";
        try {
            restTemplate.postForObject(url, user, User.class);
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
}