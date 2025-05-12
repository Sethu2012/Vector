package com.taskmanagement.controller;

import com.taskmanagement.model.PersonalTaskDTO;
import com.taskmanagement.service.PersonalTaskFrontendService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/myTasks")
public class PersonalTaskFrontendController {

    private final PersonalTaskFrontendService personalTaskService;

    public PersonalTaskFrontendController(PersonalTaskFrontendService taskService) {
        this.personalTaskService = taskService;
    }

    @GetMapping
    public String showMyTasksPage(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("jwtToken");
    	System.out.println(token);
        try {
            Map<String, List<PersonalTaskDTO>> tasks = personalTaskService.getPersonalTasksForUser(token);
            model.addAttribute("pastDatesTasks", tasks.getOrDefault("pastDatesTasks", Collections.emptyList()));
            model.addAttribute("todayTasks", tasks.getOrDefault("todayTasks", Collections.emptyList()));
            model.addAttribute("thisWeekTasks", tasks.getOrDefault("thisWeekTasks", Collections.emptyList()));
            model.addAttribute("nextWeekTasks", tasks.getOrDefault("nextWeekTasks", Collections.emptyList()));
            model.addAttribute("laterTasks", tasks.getOrDefault("laterTasks", Collections.emptyList()));
            model.addAttribute("withoutDateTasks", tasks.getOrDefault("withoutDateTasks", Collections.emptyList()));
            model.addAttribute("activePage", "myTasks");
            model.addAttribute("jwtToken", token);
            model.addAttribute("personalTaskDTO", new PersonalTaskDTO());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load tasks: " + e.getMessage());
            model.addAttribute("pastDatesTasks", Collections.emptyList());
            model.addAttribute("todayTasks", Collections.emptyList());
            model.addAttribute("thisWeekTasks", Collections.emptyList());
            model.addAttribute("nextWeekTasks", Collections.emptyList());
            model.addAttribute("laterTasks", Collections.emptyList());
            model.addAttribute("withoutDateTasks", Collections.emptyList());
            model.addAttribute("activePage", "myTasks");
            model.addAttribute("jwtToken", token);
            model.addAttribute("personalTaskDTO", new PersonalTaskDTO());
        }
        return "myTasks";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute PersonalTaskDTO taskDTO, HttpSession session, Model model) {
        String token = getTokenFromSession(session);
        if (token == null) {
            return "redirect:/login?error=Authentication token is missing";
        }

        try {
            personalTaskService.createTask(taskDTO, token);
            return "redirect:/myTasks";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create task: " + e.getMessage());
            model.addAttribute("jwtToken", token);
            return showMyTasksPage(session, model);
        }
    }

    @PostMapping("/updateStatus/{taskId}")
    public String updateTaskStatus(@PathVariable Long taskId,
                                   @RequestParam boolean isCompleted,
                                   HttpSession session,
                                   Model model) {
        String token = getTokenFromSession(session);
        if (token == null) {
            return "redirect:/login?error=Authentication token is missing";
        }

        try {
            personalTaskService.updateTaskStatus(taskId, isCompleted, token);
            return "redirect:/myTasks";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update task status: " + e.getMessage());
            model.addAttribute("jwtToken", token);
            return showMyTasksPage(session, model);
        }
    }

    @PostMapping("/updatePin/{taskId}")
    public String updateTaskPinStatus(@PathVariable Long taskId,
                                      @RequestParam boolean isPinned,
                                      HttpSession session,
                                      Model model) {
        String token = getTokenFromSession(session);
        if (token == null) {
            return "redirect:/login?error=Authentication token is missing";
        }

        try {
            personalTaskService.updateTaskPinStatus(taskId, isPinned, token);
            return "redirect:/myTasks";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update pin status: " + e.getMessage());
            model.addAttribute("jwtToken", token);
            return showMyTasksPage(session, model);
        }
    }

    // Utility method to get token from session
    private String getTokenFromSession(HttpSession session) {
        Object tokenObj = session.getAttribute("jwtToken");
        if (tokenObj != null && tokenObj instanceof String token && !token.isBlank()) {
            return token;
        }
        return null;
    }
}
