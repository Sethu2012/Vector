package com.taskmanagement.Controller;

import com.taskmanagement.Service.PersonalTaskService;
import com.taskmanagement.Service.TaskService;
import com.taskmanagement.model.PersonalTaskDTO;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.Repository.UserRepository;
import com.taskmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pinned-tasks")
public class PinnedTaskController {

    @Autowired
    private PersonalTaskService personalTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPinnedTasks(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByWorkEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch pinned personal tasks
        List<PersonalTaskDTO> pinnedPersonalTasks = personalTaskService.getPinnedPersonalTasks(email);

        // Fetch pinned project tasks grouped by project
        Map<Project, List<Task>> pinnedProjectTasksGrouped = taskService.getPinnedProjectTasksGroupedByProject(email);

        List<Map<String, Object>> projectsWithPinnedTasks = new ArrayList<>();
        for (Map.Entry<Project, List<Task>> entry : pinnedProjectTasksGrouped.entrySet()) {
            Project project = entry.getKey();
            List<Task> tasks = entry.getValue();
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("projectId", project.getProjectId());
            projectMap.put("projectName", project.getProjectName());
            projectMap.put("projectCode", project.getProjectCode());
            projectMap.put("startDate", project.getStartDate());
            projectMap.put("deadline", project.getDeadline());
            projectMap.put("priority", project.getPriority().toString());
            projectMap.put("status", project.getStatus().toString());
            projectMap.put("completionPercentage", project.getCompletionPercentage());
            projectMap.put("assignedBy", project.getCreatedBy().getFirstName() + " " + project.getCreatedBy().getLastName());

            List<Map<String, Object>> taskMaps = tasks.stream().map(task -> {
                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("taskId", task.getTaskId());
                taskMap.put("taskName", task.getTaskName());
                taskMap.put("taskCode", task.getTaskCode());
                taskMap.put("description", task.getDescription());
                taskMap.put("priority", task.getPriority().toString());
                taskMap.put("status", task.getStatus().toString());
                taskMap.put("dueDate", task.getDueDate());
                taskMap.put("startDate", task.getStartDate());
                taskMap.put("completionPercentage", task.getCompletionPercentage());
                taskMap.put("estimatedHours", task.getEstimatedHours());
                taskMap.put("actualHours", task.getActualHours());
                taskMap.put("isPinned", task.getIsPinned());
                return taskMap;
            }).collect(Collectors.toList());
            projectMap.put("pinnedTasks", taskMaps);
            projectsWithPinnedTasks.add(projectMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("pinnedPersonalTasks", pinnedPersonalTasks);
        response.put("projectsWithPinnedTasks", projectsWithPinnedTasks);

        return ResponseEntity.ok(response);
    }
}