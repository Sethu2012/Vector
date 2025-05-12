package com.taskmanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.model.PersonalTaskDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PersonalTaskFrontendService {

    private final RestTemplate restTemplate;

    @Value("${backend.api.url:http://localhost:8083}")
    private String backendApiUrl;

    public PersonalTaskFrontendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PersonalTaskDTO createTask(PersonalTaskDTO taskDTO, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        HttpEntity<PersonalTaskDTO> request = new HttpEntity<>(taskDTO, headers);

        ResponseEntity<PersonalTaskDTO> response = restTemplate.exchange(
            backendApiUrl + "/api/personal-tasks",
            HttpMethod.POST,
            request,
            PersonalTaskDTO.class
        );
        return response.getBody();
    }

    public Map<String, List<PersonalTaskDTO>> getPersonalTasksForUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            backendApiUrl + "/api/personal-tasks/my-tasks",
            HttpMethod.GET,
            request,
            Map.class
        );
        Map<String, List<PersonalTaskDTO>> tasks = response.getBody();
        return tasks != null ? tasks : Collections.emptyMap();
    }

    public void updateTaskStatus(Long taskId, boolean isCompleted, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        Map<String, Boolean> requestBody = Collections.singletonMap("isCompleted", isCompleted);
        HttpEntity<Map<String, Boolean>> request = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(
            backendApiUrl + "/api/personal-tasks/" + taskId + "/status",
            HttpMethod.PUT,
            request,
            Void.class
        );
    }

    public void updateTaskPinStatus(Long taskId, boolean isPinned, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        Map<String, Boolean> requestBody = Collections.singletonMap("isPinned", isPinned);
        HttpEntity<Map<String, Boolean>> request = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(
            backendApiUrl + "/api/personal-tasks/" + taskId + "/pin",
            HttpMethod.PUT,
            request,
            Void.class
        );
    }
}