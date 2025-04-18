package com.taskmanagement.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.model.Team;

@Service
public class TeamService {

    private final String BACKEND_API_URL = "http://localhost:8083/api/teams";

    @Autowired
    private RestTemplate restTemplate;

    public List<Team> getTeamsByProjectId(Long projectId) {
        Team[] teamsArray = restTemplate.getForObject(BACKEND_API_URL + "/project/" + projectId, Team[].class);
        return Arrays.asList(teamsArray != null ? teamsArray : new Team[0]);
    }

    public Team getTeamById(Long teamId) {
        return restTemplate.getForObject(BACKEND_API_URL + "/" + teamId, Team.class);
    }

    public Team addTeam(Team team) {
        return restTemplate.postForObject(BACKEND_API_URL, team, Team.class);
    }

    public void deleteTeam(Long teamId) {
        restTemplate.delete(BACKEND_API_URL + "/" + teamId);
    }
}
