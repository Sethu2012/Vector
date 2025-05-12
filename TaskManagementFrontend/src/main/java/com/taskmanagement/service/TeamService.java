package com.taskmanagement.service;

import com.taskmanagement.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private RestTemplate restTemplate;

    public List<Team> getTeamsByProjectId(Long projectId, String jwtToken) {
        String url = "http://localhost:8083/api/teams?projectId=" + projectId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Team[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Team[].class);
        return Arrays.asList(response.getBody());
    }

    public Team getTeamById(Long teamId, String jwtToken) {
        String url = "http://localhost:8083/api/teams/" + teamId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Team> response = restTemplate.exchange(url, HttpMethod.GET, entity, Team.class);
        return response.getBody();
    }

    public void addTeam(Team team, String jwtToken) {
        String url = "http://localhost:8083/api/teams";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<Team> entity = new HttpEntity<>(team, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    public void deleteTeam(Long teamId, String jwtToken) {
        String url = "http://localhost:8083/api/teams/" + teamId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}