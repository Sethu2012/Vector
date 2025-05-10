package com.taskmanagement.Service;

import com.taskmanagement.model.PersonalTask;
import com.taskmanagement.model.PersonalTaskDTO;
import com.taskmanagement.model.User;
import com.taskmanagement.Repository.PersonalTaskRepository;
import com.taskmanagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonalTaskService {

    @Autowired
    private PersonalTaskRepository personalTaskRepository;

    @Autowired
    private UserRepository userRepository;

    public PersonalTaskDTO createTask(PersonalTaskDTO taskDTO, String workEmail) {
        User user = userRepository.findByWorkEmail(workEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + workEmail));

        PersonalTask task = new PersonalTask();
        task.setTaskName(taskDTO.getTaskName());
        task.setTaskCode(taskDTO.getTaskCode());
        task.setTaskDate(taskDTO.getTaskdate());
        task.setPriority(taskDTO.getPriority() != null ? PersonalTask.Priority.valueOf(taskDTO.getPriority()) : null);
        task.setComplexity(taskDTO.getComplexity() != null ? PersonalTask.Complexity.valueOf(taskDTO.getComplexity()) : null);
        task.setDescription(taskDTO.getDescription());
        task.setIsPinned(taskDTO.getIsPinned() != null ? taskDTO.getIsPinned() : false);
        task.setIsCompleted(taskDTO.getIsCompleted() != null ? taskDTO.getIsCompleted() : false);
        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        try {
            PersonalTask savedTask = personalTaskRepository.save(task);
            return new PersonalTaskDTO(savedTask);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create task: " + e.getMessage(), e);
        }
    }

    public Map<String, List<PersonalTaskDTO>> getPersonalTasksForUser(String workEmail) {
        User user = userRepository.findByWorkEmail(workEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + workEmail));
        List<PersonalTask> tasks = personalTaskRepository.findByUser(user);

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        LocalDate startOfNextWeek = endOfWeek.plusDays(1);
        LocalDate endOfNextWeek = startOfNextWeek.plusDays(6);

        Map<String, List<PersonalTaskDTO>> categorizedTasks = new HashMap<>();
        categorizedTasks.put("pastDatesTasks", tasks.stream()
            .filter(task -> task.getTaskDate() != null && task.getTaskDate().isBefore(today))
            .map(PersonalTaskDTO::new)
            .collect(Collectors.toList()));
        categorizedTasks.put("todayTasks", tasks.stream()
            .filter(task -> task.getTaskDate() != null && task.getTaskDate().isEqual(today))
            .map(PersonalTaskDTO::new)
            .collect(Collectors.toList()));
        categorizedTasks.put("thisWeekTasks", tasks.stream()
            .filter(task -> task.getTaskDate() != null && !task.getTaskDate().isBefore(startOfWeek) && !task.getTaskDate().isAfter(endOfWeek))
            .map(PersonalTaskDTO::new)
            .collect(Collectors.toList()));
        categorizedTasks.put("nextWeekTasks", tasks.stream()
            .filter(task -> task.getTaskDate() != null && !task.getTaskDate().isBefore(startOfNextWeek) && !task.getTaskDate().isAfter(endOfNextWeek))
            .map(PersonalTaskDTO::new)
            .collect(Collectors.toList()));
        categorizedTasks.put("laterTasks", tasks.stream()
            .filter(task -> task.getTaskDate() != null && task.getTaskDate().isAfter(endOfNextWeek))
            .map(PersonalTaskDTO::new)
            .collect(Collectors.toList()));
        categorizedTasks.put("withoutDateTasks", tasks.stream()
            .filter(task -> task.getTaskDate() == null)
            .map(PersonalTaskDTO::new)
            .collect(Collectors.toList()));

        return categorizedTasks;
    }

    public void updateTaskStatus(Long taskId, boolean isCompleted, String workEmail) {
        PersonalTask task = personalTaskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findByWorkEmail(workEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + workEmail));

        if (!task.getUser().getWorkEmail().equals(workEmail)) {
            throw new RuntimeException("Unauthorized: User does not own this task");
        }

        task.setIsCompleted(isCompleted);
        task.setCompletionDate(isCompleted ? LocalDateTime.now() : null);
        task.setCompletedBy(isCompleted ? user : null);
        task.setUpdatedAt(LocalDateTime.now());
        personalTaskRepository.save(task);
    }

    public void updateTaskPinStatus(Long taskId, boolean isPinned, String workEmail) {
        PersonalTask task = personalTaskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findByWorkEmail(workEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + workEmail));

        if (!task.getUser().getWorkEmail().equals(workEmail)) {
            throw new RuntimeException("Unauthorized: User does not own this task");
        }

        task.setIsPinned(isPinned);
        task.setUpdatedAt(LocalDateTime.now());
        personalTaskRepository.save(task);
    }

    public List<PersonalTaskDTO> getPinnedPersonalTasks(String workEmail) {
        User user = userRepository.findByWorkEmail(workEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + workEmail));
        List<PersonalTask> pinnedTasks = personalTaskRepository.findByUserAndIsPinnedTrue(user);
        return pinnedTasks.stream().map(PersonalTaskDTO::new).collect(Collectors.toList());
    }
}