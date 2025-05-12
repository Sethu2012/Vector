package com.taskmanagement.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.taskmanagement.model.Project;
import com.taskmanagement.service.ProjectService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String listProjects(Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }
        try {
            List<Project> projects = projectService.getAllProjects(jwtToken);
            System.out.println("=== Projects Received ===");
            projects.forEach(System.out::println);
            model.addAttribute("projects", projects);
            model.addAttribute("jwtToken", jwtToken);
            System.out.println("JWT Token: " + jwtToken);
        } catch (HttpClientErrorException e) {
            // Handle 401 Unauthorized or other HTTP errors
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            model.addAttribute("error", "Failed to load projects: " + e.getMessage());
            model.addAttribute("projects", Collections.emptyList()); // Prevent null
            model.addAttribute("jwtToken", jwtToken);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load projects: " + e.getMessage());
            model.addAttribute("projects", Collections.emptyList()); // Prevent null
            model.addAttribute("jwtToken", jwtToken);
        }
        return "project";
    }

    @PostMapping("/add")
    public String addProject(@ModelAttribute("projectForm") Project project, HttpSession session, Model model) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }
        try {
            projectService.addProject(project, jwtToken);
            return "redirect:/projects";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            model.addAttribute("error", "Failed to add project: " + e.getMessage());
            return "project"; // Return to project page with error
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add project: " + e.getMessage());
            return "project"; // Return to project page with error
        }
    }

    @PostMapping("/{id}")
    public String getProjectById(@PathVariable Long id, Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }
        try {
            Project project = projectService.getProjectById(id, jwtToken);
            model.addAttribute("project", project);
            model.addAttribute("jwtToken", jwtToken);
            return "project-view";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            model.addAttribute("error", "Project with ID " + id + " not found: " + e.getMessage());
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Project with ID " + id + " not found: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }
        try {
            projectService.deleteProject(id, jwtToken);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            // Log error but continue with redirect
            System.err.println("Failed to delete project: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to delete project: " + e.getMessage());
        }
        return "redirect:/projects";
    }
}