package com.taskmanagement.Controller;

import com.taskmanagement.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getProjects(Authentication authentication) {
        List<Map<String, Object>> projects = projectService.getProjectsForUser(authentication);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Map<String, Object>> getProjectDetails(@PathVariable Long projectId, Authentication authentication) {
        Map<String, Object> projectDetails = projectService.getProjectDetails(projectId, authentication);
        return ResponseEntity.ok(projectDetails);
    }
}