package com.taskmanagement.Controller;

import com.taskmanagement.Repository.PersonalTaskRepository;
import com.taskmanagement.Repository.ProjectRepository;
import com.taskmanagement.Repository.TaskRepository;
import com.taskmanagement.Repository.UserRepository;
import com.taskmanagement.model.PersonalTask;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private PersonalTaskRepository personalTaskRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/stats")
    public Map<String, Object> getHomeStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> stats = new HashMap<>();
        
        // My Tasks count (personal tasks)
        long personalTasksCount = personalTaskRepository.countByUser(user);
        stats.put("myTasksCount", personalTasksCount);
        
        // Due Today count
        LocalDate today = LocalDate.now();
        long tasksDueToday = taskRepository.countByAssigneeAndDueDate(user, today);
        long personalTasksDueToday = personalTaskRepository.countByUserAndTaskDate(user, today);
        stats.put("dueTodayCount", tasksDueToday + personalTasksDueToday);
        
        // Projects count
        long projectsCount = projectRepository.countByManager(user);
        stats.put("projectsCount", projectsCount);
        
        // Overdue count
        long overdueTasks = taskRepository.countByAssigneeAndDueDateBeforeAndStatusNot(
                user, today, Task.TaskStatus.Completed);
        long overduePersonalTasks = personalTaskRepository.countByUserAndTaskDateBeforeAndIsCompletedFalse(
                user, today);
        stats.put("overdueCount", overdueTasks + overduePersonalTasks);
        
        // Recent tasks (created in last 24 hours)
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<Task> recentTasks = taskRepository.findByAssigneeAndCreatedAtAfter(user, yesterday);
        List<PersonalTask> recentPersonalTasks = personalTaskRepository.findByUserAndCreatedAtAfter(user, yesterday);
        
        // Combine and transform tasks for display
        List<Map<String, Object>> combinedRecentTasks = recentTasks.stream()
                .map(this::transformTask)
                .collect(Collectors.toList());
        
        recentPersonalTasks.stream()
                .map(this::transformPersonalTask)
                .forEach(combinedRecentTasks::add);
        
        stats.put("recentTasks", combinedRecentTasks);
        
        // Due today tasks
        List<Task> dueTodayTasks = taskRepository.findByAssigneeAndDueDate(user, today);
        List<PersonalTask> personalDueTodayTasks = personalTaskRepository.findByUserAndTaskDate(user, today);
        
        List<Map<String, Object>> combinedDueTodayTasks = dueTodayTasks.stream()
                .map(this::transformTask)
                .collect(Collectors.toList());
        
        personalDueTodayTasks.stream()
                .map(this::transformPersonalTask)
                .forEach(combinedDueTodayTasks::add);
        
        stats.put("dueTodayTasks", combinedDueTodayTasks);
        
        // Overdue tasks
        List<Task> overdueTaskList = taskRepository.findByAssigneeAndDueDateBeforeAndStatusNot(
                user, today, Task.TaskStatus.Completed);
        List<PersonalTask> overduePersonalTaskList = personalTaskRepository.findByUserAndTaskDateBeforeAndIsCompletedFalse(
                user, today);
        
        List<Map<String, Object>> combinedOverdueTasks = overdueTaskList.stream()
                .map(this::transformTask)
                .collect(Collectors.toList());
        
        overduePersonalTaskList.stream()
                .map(this::transformPersonalTask)
                .forEach(combinedOverdueTasks::add);
        
        stats.put("overdueTasks", combinedOverdueTasks);
        
        // User info
        stats.put("userName", user.getFirstName() + " " + user.getLastName());
        
        return stats;
    }
    
    private Map<String, Object> transformTask(Task task) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("taskId", task.getTaskId());
        taskMap.put("taskName", task.getTaskName());
        taskMap.put("createdAt", task.getCreatedAt());
        taskMap.put("dueDate", task.getDueDate());
        taskMap.put("status", task.getStatus().toString());
        if (task.getCreator() != null) {
            taskMap.put("creatorId", task.getCreator().getUserId());
        }
        return taskMap;
    }
    
    private Map<String, Object> transformPersonalTask(PersonalTask personalTask) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("taskId", personalTask.getPersonalTaskId());
        taskMap.put("taskName", personalTask.getTaskName());
        taskMap.put("createdAt", personalTask.getCreatedAt());
        taskMap.put("dueDate", personalTask.getTaskDate());
        taskMap.put("status", personalTask.getIsCompleted() ? "Completed" : "Not Completed");
        taskMap.put("creatorId", personalTask.getUser().getUserId());
        return taskMap;
    }
}