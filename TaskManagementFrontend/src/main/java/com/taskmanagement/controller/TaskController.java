package com.taskmanagement.controller;

import com.taskmanagement.model.Task;
import com.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String showTaskViewPage(Model model, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            return "redirect:/login?error=No token found";
        }

        try {
            Map<String, List<Task>> categorizedTasks = taskService.getCategorizedTasks(jwtToken);
            System.out.println("=== Categorized Tasks Received ===");
            categorizedTasks.forEach((category, tasks) -> {
                System.out.println(category + ":");
                tasks.forEach(System.out::println);
            });

            model.addAttribute("notYetStartedTasks", categorizedTasks.getOrDefault("Not_Yet_Started", Collections.emptyList()));
            model.addAttribute("onProgressTasks", categorizedTasks.getOrDefault("On_Progress", Collections.emptyList()));
            model.addAttribute("underReviewTasks", categorizedTasks.getOrDefault("Under_Review", Collections.emptyList()));
            model.addAttribute("onHoldTasks", categorizedTasks.getOrDefault("On_Hold", Collections.emptyList()));
            model.addAttribute("blockedTasks", categorizedTasks.getOrDefault("Blocked", Collections.emptyList()));
            model.addAttribute("completedTasks", categorizedTasks.getOrDefault("Completed", Collections.emptyList()));
            model.addAttribute("jwtToken", jwtToken);
            System.out.println("JWT Token: " + jwtToken);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                session.invalidate();
                return "redirect:/login?error=Invalid or expired token";
            }
            model.addAttribute("error", "Failed to load tasks: " + e.getMessage());
            model.addAttribute("notYetStartedTasks", Collections.emptyList());
            model.addAttribute("onProgressTasks", Collections.emptyList());
            model.addAttribute("underReviewTasks", Collections.emptyList());
            model.addAttribute("onHoldTasks", Collections.emptyList());
            model.addAttribute("blockedTasks", Collections.emptyList());
            model.addAttribute("completedTasks", Collections.emptyList());
            model.addAttribute("jwtToken", jwtToken);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load tasks: " + e.getMessage());
            model.addAttribute("notYetStartedTasks", Collections.emptyList());
            model.addAttribute("onProgressTasks", Collections.emptyList());
            model.addAttribute("underReviewTasks", Collections.emptyList());
            model.addAttribute("onHoldTasks", Collections.emptyList());
            model.addAttribute("blockedTasks", Collections.emptyList());
            model.addAttribute("completedTasks", Collections.emptyList());
            model.addAttribute("jwtToken", jwtToken);
        }

        return "task-view";
    }
}