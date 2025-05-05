package com.taskmanagement.Controller;

import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskAttachment;
import com.taskmanagement.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

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
        Comment comment = taskService.addTaskComment(taskId, content, email);
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
}