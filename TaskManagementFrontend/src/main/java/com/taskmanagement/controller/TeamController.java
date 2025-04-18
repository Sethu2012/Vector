package com.taskmanagement.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.taskmanagement.model.Team;
import com.taskmanagement.service.TeamService;

@Controller
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    // Display all teams for a project
    @GetMapping("/project/{projectId}")
    public String getTeamsByProject(@PathVariable Long projectId, Model model) {
        List<Team> teams = teamService.getTeamsByProjectId(projectId);
        model.addAttribute("teams", teams);
        model.addAttribute("projectId", projectId);
        return "project-view"; // Reuse project-view.html to display teams
    }

    // Process the add team form submission
    @PostMapping("/add")
    public String addTeam(@ModelAttribute("team") Team team) {
        teamService.addTeam(team);
        return "redirect:/teams/project/" + team.getProject().getProjectId();
    }

    // Delete a team
    @GetMapping("/delete/{teamId}")
    public String deleteTeam(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        Long projectId = team.getProject().getProjectId();
        teamService.deleteTeam(teamId);
        return "redirect:/teams/project/" + projectId;
    }
}
