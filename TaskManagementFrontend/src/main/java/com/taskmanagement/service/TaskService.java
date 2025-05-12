package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.Task.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TaskService {

    private static final String BACKEND_API_URL = "http://localhost:8083/api/tasks";

    @Autowired
    private RestTemplate restTemplate;

    public List<Task> getAllTasks(String jwtToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println("Fetching all tasks with token: " + jwtToken);
            ResponseEntity<Task[]> response = restTemplate.exchange(
                BACKEND_API_URL,
                HttpMethod.GET,
                entity,
                Task[].class
            );
            System.out.println("Response Status: " + response.getStatusCode());
            return response.getBody() != null ? Arrays.asList(response.getBody()) : new ArrayList<>();
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error fetching all tasks: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (RestClientException e) {
            System.err.println("RestClientException fetching all tasks: " + e.getMessage());
            throw new RuntimeException("Failed to fetch all tasks: " + e.getMessage(), e);
        }
    }

    public List<Task> getTasksByStatus(String status, String jwtToken) {
        try {
            TaskStatus taskStatus = convertToTaskStatus(status);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println("Fetching tasks by status " + taskStatus + " with token: " + jwtToken);
            ResponseEntity<Task[]> response = restTemplate.exchange(
                BACKEND_API_URL + "/status/" + taskStatus,
                HttpMethod.GET,
                entity,
                Task[].class
            );
            System.out.println("Response Status: " + response.getStatusCode());
            return response.getBody() != null ? Arrays.asList(response.getBody()) : new ArrayList<>();
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error fetching tasks by status: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (RestClientException e) {
            System.err.println("RestClientException fetching tasks by status: " + e.getMessage());
            throw new RuntimeException("Failed to fetch tasks by status " + status + ": " + e.getMessage(), e);
        }
    }

    public Map<String, List<Task>> getCategorizedTasks(String jwtToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println("Fetching categorized tasks with token: " + jwtToken);
            ResponseEntity<Map<String, List<Task>>> response = restTemplate.exchange(
                BACKEND_API_URL + "/categorized",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, List<Task>>>() {}
            );
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error fetching categorized tasks: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (RestClientException e) {
            System.err.println("RestClientException fetching categorized tasks: " + e.getMessage());
            throw new RuntimeException("Failed to fetch categorized tasks: " + e.getMessage(), e);
        }
    }

    private TaskStatus convertToTaskStatus(String statusStr) {
        if (statusStr == null || statusStr.trim().isEmpty()) {
            return TaskStatus.Not_Yet_Started;
        }
        try {
            return TaskStatus.valueOf(statusStr.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task status: " + statusStr +
                ". Expected: " + Arrays.toString(TaskStatus.values()), e);
        }
    }
}