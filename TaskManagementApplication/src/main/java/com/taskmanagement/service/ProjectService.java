package com.taskmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanagement.model.Project;
import com.taskmanagement.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Save a new project
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }


    // Delete a project by ID
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}

