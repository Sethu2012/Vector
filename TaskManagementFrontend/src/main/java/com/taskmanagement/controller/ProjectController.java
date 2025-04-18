package com.taskmanagement.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.taskmanagement.model.Project;
import com.taskmanagement.service.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Display the dashboard with projects (the Thymeleaf template uses the provided HTML code)
    @GetMapping
    public String listProjects(Model model) {
        System.out.println("=== Entered /projects mapping ==="); // Make sure controller is hit

        List<Project> projects = projectService.getAllProjects();

        System.out.println("=== Projects Received ===");
        projects.forEach(System.out::println); // Check if this prints

        model.addAttribute("projects", projects);
        return "project";
    }


    // Process the add project form submission
    @PostMapping("/add")
    public String addProject(@ModelAttribute("projectForm") Project project) {
        projectService.addProject(project);
        return "redirect:/projects";
    }
    
    
    @GetMapping("/{id}")
    public String getProjectById(@PathVariable Long id, Model model) {
        try {
            Project project = projectService.getProjectById(id);
            model.addAttribute("project", project);
            return "project-view";
        } catch (Exception e) {
            model.addAttribute("error", "Project with ID " + id + " not found.");
            return "error";
        }
    }


    // Delete a project by its id
    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
}

