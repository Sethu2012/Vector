package com.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanagement.model.Team;
import com.taskmanagement.service.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Team>> getTeamsByProject(@PathVariable Long projectId) {
        List<Team> teams = teamService.getTeamsByProjectId(projectId);
        if (teams.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        }
        return new ResponseEntity<>(teams, HttpStatus.OK); 
    }

    @PostMapping("/add")
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        if (team == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
        teamService.addTeam(team);
        return new ResponseEntity<>(team, HttpStatus.CREATED); 
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        Long projectId = team.getProject().getProjectId();
        teamService.deleteTeam(teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
}
