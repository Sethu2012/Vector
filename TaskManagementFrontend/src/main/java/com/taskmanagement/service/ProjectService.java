package com.taskmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.model.Project;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {

    // Set your backend API URL (adjust the port if needed)
    private final String BACKEND_API_URL = "http://localhost:8083/api/projects";
    
    @Autowired
    private  RestTemplate restTemplate;

    public List<Project> getAllProjects() {
        Project[] projectsArray = restTemplate.getForObject(BACKEND_API_URL, Project[].class);

        return Arrays.asList(projectsArray != null ? projectsArray : new Project[0]);
    }


    // Add a new project via the backend API
    public Project addProject(Project project) {
        return restTemplate.postForObject(BACKEND_API_URL, project, Project.class);
    }

    public Project getProjectById(Long id) {
        return restTemplate.getForObject(BACKEND_API_URL + "/" + id, Project.class);
    }
    
    // Delete a project by its id
    public void deleteProject(Long id) {
        String deleteUrl = BACKEND_API_URL + "/" + id;
        restTemplate.delete(deleteUrl);
    }
}

