package com.project2_taskmanagement.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class MyProjectsService {
    private static final Logger logger = LoggerFactory.getLogger(MyProjectsService.class);

    @Autowired
    private RestTemplate restTemplate;

    private static final String PROJECTS_API_URL = "http://localhost:8083/api/projects";
    private static final String TASK_COMPLETION_API_URL = "http://localhost:8083/api/tasks/%d/completion";
    private static final String TASK_PINNED_API_URL = "http://localhost:8083/api/tasks/%d/pinned";
    private static final String TASK_COMMENTS_API_URL = "http://localhost:8083/api/tasks/%d/comments";

    public List<Map<String, Object>> fetchProjects(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map[]> response = restTemplate.exchange(
                PROJECTS_API_URL, HttpMethod.GET, entity, Map[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            throw new RuntimeException("Failed to fetch projects from API");
        }
    }

    public void updateTaskCompletion(Long taskId, Integer completionPercentage, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Map<String, Integer> requestBody = Map.of("completionPercentage", completionPercentage);
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                String.format(TASK_COMPLETION_API_URL, taskId), HttpMethod.PUT, entity, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to update task completion: " + response.getStatusCode());
        }
    }

    public void updateTaskPinnedStatus(Long taskId, Boolean isPinned, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Map<String, Boolean> requestBody = Map.of("isPinned", isPinned);
        HttpEntity<Map<String, Boolean>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                String.format(TASK_PINNED_API_URL, taskId), HttpMethod.PUT, entity, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to update pinned status: " + response.getStatusCode());
        }
    }

    public void addTaskComment(Long taskId, String content, String mentions, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = Map.of("content", content, "mentions", mentions);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                String.format(TASK_COMMENTS_API_URL, taskId), HttpMethod.POST, entity, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to add comment: " + response.getStatusCode());
        }
    }

    public List<Map<String, Object>> getTaskComments(Long taskId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map[]> response = restTemplate.exchange(
                String.format(TASK_COMMENTS_API_URL, taskId), HttpMethod.GET, entity, Map[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            throw new RuntimeException("Failed to fetch comments from API");
        }
    }
}