package com.taskmanagement.controller;


import com.taskmanagement.model.Project;
import com.taskmanagement.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogsController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/completed")
    public List<Project> getCompletedProjects() {
        return projectService.getCompletedProjects();
    }
}

