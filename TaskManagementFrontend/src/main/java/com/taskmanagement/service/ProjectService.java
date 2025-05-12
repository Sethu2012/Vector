package com.taskmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.model.Project;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {

    private final String BACKEND_API_URL = "http://localhost:8083/api/projects";
    
    @Autowired
    private RestTemplate restTemplate;

    public List<Project> getAllProjects(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("JWT token is missing");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            Project[] projectsArray = restTemplate.exchange(
                BACKEND_API_URL, 
                HttpMethod.GET, 
                entity, 
                Project[].class
            ).getBody();
            return Arrays.asList(projectsArray != null ? projectsArray : new Project[0]);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch projects: " + e.getStatusCode() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching projects: " + e.getMessage(), e);
        }
    }

    public Project addProject(Project project, String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("JWT token is missing");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<Project> entity = new HttpEntity<>(project, headers);
            return restTemplate.postForObject(BACKEND_API_URL, entity, Project.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to add project: " + e.getStatusCode() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while adding project: " + e.getMessage(), e);
        }
    }

    public Project getProjectById(Long id, String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("JWT token is missing");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(
                BACKEND_API_URL + "/" + id, 
                HttpMethod.GET, 
                entity, 
                Project.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to fetch project with ID " + id + ": " + e.getStatusCode() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching project with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public void deleteProject(Long id, String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("JWT token is missing");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            restTemplate.exchange(
                BACKEND_API_URL + "/" + id, 
                HttpMethod.DELETE, 
                entity, 
                Void.class
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to delete project with ID " + id + ": " + e.getStatusCode() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while deleting project with ID " + id + ": " + e.getMessage(), e);
        }
    }
}