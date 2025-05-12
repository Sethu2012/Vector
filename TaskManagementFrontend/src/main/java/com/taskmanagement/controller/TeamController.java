package com.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.taskmanagement.model.Project;
import com.taskmanagement.model.Team;
import com.taskmanagement.service.ProjectService;
import com.taskmanagement.service.TeamService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/project/{projectId}")
    public String getTeamsByProject(@PathVariable Long projectId, Model model, HttpSession session) {
        // Validate JWT token
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }

        try {
            // Fetch the project details
            Project project = projectService.getProjectById(projectId, jwtToken);
            model.addAttribute("project", project);

            // Fetch teams for the project
            List<Team> teams = teamService.getTeamsByProjectId(projectId, jwtToken);
            model.addAttribute("teams", teams);
            model.addAttribute("jwtToken", jwtToken);

            // Debugging output
            System.out.println("Teams for Project: " + project.getProjectName());
            for (Team team : teams) {
                System.out.println("Team ID: " + team.getTeamId());
                System.out.println("Team Name: " + team.getTeamName());
                System.out.println("-----");
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            model.addAttribute("error", "Failed to load project or teams: " + e.getMessage());
            return "project-view";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "project-view";
        }

        return "project-view";
    }

    @PostMapping("/add/{projectId}")
    public String addTeam(@PathVariable Long projectId, @ModelAttribute Team team, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }

        try {
            Project project = projectService.getProjectById(projectId, jwtToken);
            team.setProject(project);
            teamService.addTeam(team, jwtToken);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            System.err.println("Failed to add team: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to add team: " + e.getMessage());
        }

        return "redirect:/teams/project/" + projectId;
    }

    @GetMapping("/delete/{teamId}")
    public String deleteTeam(@PathVariable Long teamId, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }

        try {
            Team team = teamService.getTeamById(teamId, jwtToken);
            Long projectId = team.getProject().getProjectId();
            teamService.deleteTeam(teamId, jwtToken);
            return "redirect:/teams/project/" + projectId;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            System.err.println("Failed to delete team: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to delete team: " + e.getMessage());
        }

        // Fallback redirect in case of error (team might not have a project ID)
        return "redirect:/projects";
    }
}