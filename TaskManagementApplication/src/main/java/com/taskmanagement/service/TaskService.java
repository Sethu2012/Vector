package com.taskmanagement.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.Task.TaskStatus;
import com.taskmanagement.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Map<String, List<Task>> getCategorizedTasks() {
        Map<String, List<Task>> categorizedTasks = new LinkedHashMap<>();
        for (Task.TaskStatus status : Task.TaskStatus.values()) {
            categorizedTasks.put(status.name(), taskRepository.findByStatus(status));
        }
        return categorizedTasks;
    }

    public Task saveTask(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Task.TaskStatus.Not_Yet_Started);
        }

        if (task.getPriority() == null) {
            task.setPriority(Task.Priority.Medium);
        }

        return taskRepository.save(task);
    }
}
