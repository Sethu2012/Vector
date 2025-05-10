package com.project2_taskmanagement.Controller;

import com.project2_taskmanagement.Service.MyProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyProjectsController {

    private static final Logger logger = LoggerFactory.getLogger(MyProjectsController.class);

    @Autowired
    private MyProjectsService myProjectsService;

    @GetMapping("/myProjects")
    public String showMyProjects(Model model, @RequestParam(value = "token", required = false) String token) {
        logger.debug("Handling /myProjects request with token: {}", token);
        if (token == null || token.trim().isEmpty()) {
            logger.warn("Authentication token is missing");
            return "redirect:/login?error=Authentication token is missing";
        }
        try {
            List<Map<String, Object>> projects = myProjectsService.fetchProjects(token);
            model.addAttribute("projects", projects);
            model.addAttribute("activePage", "myProjects");
            model.addAttribute("jwtToken", token);
            logger.info("Successfully fetched projects for token: {}", token);
        } catch (Exception e) {
            logger.error("Failed to load projects: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load projects: " + e.getMessage());
            model.addAttribute("activePage", "myProjects");
            model.addAttribute("jwtToken", token);
        }
        return "myProjects";
    }

    @PostMapping("/api/updateTaskCompletion")
    @ResponseBody
    public Map<String, String> updateTaskCompletion(@RequestBody Map<String, Object> request) {
        logger.debug("Handling /api/updateTaskCompletion request: {}", request);
        Map<String, String> response = new HashMap<>();
        try {
            Long taskId = Long.valueOf(request.get("taskId").toString());
            Integer completionPercentage = Integer.valueOf(request.get("completionPercentage").toString());
            String token = request.get("token").toString();
            myProjectsService.updateTaskCompletion(taskId, completionPercentage, token);
            response.put("message", "Task completion updated successfully");
            logger.info("Successfully updated task completion for task ID: {}", taskId);
        } catch (Exception e) {
            logger.error("Failed to update task completion: {}", e.getMessage(), e);
            response.put("message", "Failed to update task: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/api/updateTaskPinned")
    @ResponseBody
    public Map<String, String> updateTaskPinnedStatus(@RequestBody Map<String, Object> request) {
        logger.debug("Handling /api/updateTaskPinned request: {}", request);
        Map<String, String> response = new HashMap<>();
        try {
            Long taskId = Long.valueOf(request.get("taskId").toString());
            Boolean isPinned = Boolean.valueOf(request.get("isPinned").toString());
            String token = request.get("token").toString();
            myProjectsService.updateTaskPinnedStatus(taskId, isPinned, token);
            response.put("message", "Task pinned status updated successfully");
            logger.info("Successfully updated pinned status for task ID: {}", taskId);
        } catch (Exception e) {
            logger.error("Failed to update task pinned status: {}", e.getMessage(), e);
            response.put("message", "Failed to update task pinned status: " + e.getMessage());
        }
        return response;
    }
}