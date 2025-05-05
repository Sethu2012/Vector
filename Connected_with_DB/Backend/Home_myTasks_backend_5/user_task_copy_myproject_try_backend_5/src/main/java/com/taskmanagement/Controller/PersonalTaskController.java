package com.taskmanagement.Controller;

import com.taskmanagement.model.PersonalTaskDTO;
import com.taskmanagement.Service.PersonalTaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personal-tasks")
@Validated
public class PersonalTaskController {

    @Autowired
    private PersonalTaskService personalTaskService;

    @PostMapping
    public ResponseEntity<PersonalTaskDTO> createTask(@Valid @RequestBody PersonalTaskDTO taskDTO, Authentication authentication) {
        PersonalTaskDTO createdTask = personalTaskService.createTask(taskDTO, authentication.getName());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<Map<String, List<PersonalTaskDTO>>> getPersonalTasksForUser(Authentication authentication) {
        Map<String, List<PersonalTaskDTO>> tasks = personalTaskService.getPersonalTasksForUser(authentication.getName());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, Boolean> request, Authentication authentication) {
        personalTaskService.updateTaskStatus(taskId, request.get("isCompleted"), authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}/pin")
    public ResponseEntity<Void> updateTaskPinStatus(@PathVariable Long taskId, @RequestBody Map<String, Boolean> request, Authentication authentication) {
        personalTaskService.updateTaskPinStatus(taskId, request.get("isPinned"), authentication.getName());
        return ResponseEntity.ok().build();
    }
}