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

    public List<Project> getAllProjects(String userEmail) {
        // Fetch projects where the user is either the manager or creator
        return projectRepository.findByManagerWorkEmailOrCreatedByWorkEmail(userEmail, userEmail);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    
    
    
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    
    public List<Project> getCompletedProjects() {
    	return projectRepository.findByStatus(Project.ProjectStatus.Completed); 

    }

}

