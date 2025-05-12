package com.taskmanagement.service;

import com.taskmanagement.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class LogsService {

    private final String BASE_URL = "http://localhost:8083/api/logs";

    @Autowired
    private RestTemplate restTemplate;

    public List<Project> fetchCompletedLogs() {
        Project[] projects = restTemplate.getForObject(BASE_URL + "/completed", Project[].class);
        return Arrays.asList(projects);
    }
}

