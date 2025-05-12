package com.taskmanagement.controller;

import com.taskmanagement.model.Project;
import com.taskmanagement.service.LogsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LogsController {

    @Autowired
    private LogsService logsService;

    @GetMapping("/logs")
    public String showLogs(Model model) {
        List<Project> completedProjects = logsService.fetchCompletedLogs();
        model.addAttribute("completedProjects", completedProjects);
        return "logs"; 
    }
}


