package com.taskmanagement.Controller;

import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskAttachment;
import com.taskmanagement.model.User;
import com.taskmanagement.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private com.taskmanagement.Repository.TaskRepository taskRepository;

    @Autowired
    private com.taskmanagement.Repository.UserRepository userRepository;

    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<Map<String, Object>>> getTasksByProject(
            @PathVariable Long projectId, Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching tasks for project ID: {} by user: {}", projectId, email);
        List<Map<String, Object>> tasks = taskService.getTasksByProject(projectId, email);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/completion")
    public ResponseEntity<Map<String, String>> updateTaskCompletion(
            @PathVariable Long taskId,
            @RequestBody Map<String, Integer> request,
            Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Updating task completion for task ID: {} by user: {}", taskId, email);
        try {
            Integer completionPercentage = request.get("completionPercentage");
            if (completionPercentage == null) {
                logger.error("Missing completionPercentage in request body");
                Map<String, String> error = new HashMap<>();
                error.put("message", "completionPercentage is required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            taskService.updateTaskCompletion(taskId, completionPercentage, email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Task completion updated successfully");
            logger.info("Task completion updated successfully for task ID: {}", taskId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update task completion for task ID: {}: {}", taskId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update task: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{taskId}/pinned")
    public ResponseEntity<Map<String, String>> updateTaskPinnedStatus(
            @PathVariable Long taskId,
            @RequestBody Map<String, Boolean> request,
            Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Updating pinned status for task ID: {} by user: {} to: {}", taskId, email, request.get("isPinned"));
        try {
            Boolean isPinned = request.get("isPinned");
            if (isPinned == null) {
                logger.error("Missing isPinned in request body");
                Map<String, String> error = new HashMap<>();
                error.put("message", "isPinned is required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            taskService.updateTaskPinnedStatus(taskId, isPinned, email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Task pinned status updated successfully");
            logger.info("Task pinned status updated successfully for task ID: {} to: {}", taskId, isPinned);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update task pinned status for task ID: {}: {}", taskId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update task pinned status: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getTaskComments(
            @PathVariable Long taskId, Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching comments for task ID: {} by user: {}", taskId, email);
        List<Comment> comments = taskService.getTaskComments(taskId, email);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Comment> addTaskComment(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Adding comment to task ID: {} by user: {}", taskId, email);
        String content = request.get("content");
        String mentions = request.get("mentions");
        Comment comment = taskService.addTaskComment(taskId, content, email, mentions);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PostMapping("/{taskId}/attachments")
    public ResponseEntity<TaskAttachment> uploadTaskAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Uploading attachment to task ID: {} by user: {}", taskId, email);
        TaskAttachment attachment = taskService.uploadTaskAttachment(taskId, file, email);
        return new ResponseEntity<>(attachment, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}/attachments")
    public ResponseEntity<List<TaskAttachment>> getTaskAttachments(
            @PathVariable Long taskId, Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching attachments for task ID: {} by user: {}", taskId, email);
        List<TaskAttachment> attachments = taskService.getTaskAttachments(taskId, email);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/project/{projectId}/team-members")
    public ResponseEntity<List<Map<String, Object>>> getTeamMembersForProject(
            @PathVariable Long projectId, Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching team members for project ID: {} by user: {}", projectId, email);
        List<Map<String, Object>> teamMembers = taskService.getTeamMembersForProject(projectId, email);
        return ResponseEntity.ok(teamMembers);
    }

    @GetMapping("/{taskId}/team-members")
    public ResponseEntity<List<String>> getTaskTeamMembers(@PathVariable Long taskId, Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching team members for task ID: {} by user: {}", taskId, email);
        List<String> teamMembers = taskService.getTaskTeamMembers(taskId, email);
        return ResponseEntity.ok(teamMembers);
    }

    // New endpoint for pinned project tasks
    @GetMapping("/pinned")
    public ResponseEntity<List<Map<String, Object>>> getPinnedProjectTasks(Authentication authentication) {
        String workEmail = authentication.getName();
        User user = userRepository.findByWorkEmail(workEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + workEmail));
        List<Task> pinnedTasks = taskRepository.findByAssigneeAndIsPinnedTrue(user);
        List<Map<String, Object>> taskMaps = pinnedTasks.stream().map(task -> {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("taskId", task.getTaskId());
            taskMap.put("taskCode", task.getTaskCode());
            taskMap.put("taskName", task.getTaskName());
            taskMap.put("dueDate", task.getDueDate());
            taskMap.put("status", task.getStatus().toString());
            taskMap.put("priority", task.getPriority().toString());
            taskMap.put("completionPercentage", task.getCompletionPercentage());
            taskMap.put("description", task.getDescription());
            taskMap.put("dependencies", task.getDependencies());
            taskMap.put("startDate", task.getStartDate());
            taskMap.put("projectName", task.getCategory().getTeam().getProject().getProjectName());
            taskMap.put("isPinned", task.getIsPinned() != null ? task.getIsPinned() : false);
            return taskMap;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(taskMaps);
    }
    @GetMapping("/pinnedTasks")
    public String showPinnedTasks(Model model, @RequestParam("token") String token) {
        model.addAttribute("jwtToken", token);
        return "pinnedTasks";
    }
}