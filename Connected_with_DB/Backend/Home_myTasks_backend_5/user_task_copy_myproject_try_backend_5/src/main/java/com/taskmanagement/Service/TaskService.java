package com.taskmanagement.Service;

import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskAttachment;
import com.taskmanagement.model.User;
import com.taskmanagement.Repository.TaskRepository;
import com.taskmanagement.Repository.UserRepository;
import com.taskmanagement.Repository.CommentRepository;
import com.taskmanagement.Repository.TaskAttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskAttachmentRepository attachmentRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public TaskService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            logger.error("Could not create upload directory: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public List<Map<String, Object>> getTasksByProject(Long projectId, String email) {
        logger.debug("Fetching tasks for project ID: {} by user: {}", projectId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Project project = new Project();
        project.setProjectId(projectId);

        List<Task> tasks = taskRepository.findByCategoryTeamProject(project);
        return tasks.stream().map(task -> {
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
            return taskMap;
        }).collect(Collectors.toList());
    }

    public void updateTaskCompletion(Long taskId, Integer completionPercentage, String email) {
        logger.debug("Updating task completion for task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        if (!task.getAssignee().getUserId().equals(user.getUserId())) {
            logger.warn("Unauthorized attempt to update task ID: {} by user: {}", taskId, email);
            throw new RuntimeException("Unauthorized: User is not the assignee of this task");
        }

        if (completionPercentage < 0 || completionPercentage > 100) {
            logger.error("Invalid completion percentage: {} for task ID: {}", completionPercentage, taskId);
            throw new RuntimeException("Invalid completion percentage");
        }

        task.setCompletionPercentage(completionPercentage);
        Task.TaskStatus status;
        if (completionPercentage == 0) {
            status = Task.TaskStatus.Not_Yet_Started;
        } else if (completionPercentage > 0 && completionPercentage <= 50) {
            status = Task.TaskStatus.On_Progress;
        } else if (completionPercentage > 50 && completionPercentage < 100) {
            status = Task.TaskStatus.Under_Review;
        } else {
            status = Task.TaskStatus.Completed;
        }
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        task.setLastStatusChange(LocalDateTime.now());
        
        try {
            taskRepository.save(task);
            logger.info("Task completion updated for task ID: {}", taskId);
        } catch (Exception e) {
            logger.error("Failed to save task ID: {}: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("Failed to save task: " + e.getMessage(), e);
        }
    }

    public List<Comment> getTaskComments(Long taskId, String email) {
        logger.debug("Fetching comments for task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        return commentRepository.findByTask(task);
    }

    public Comment addTaskComment(Long taskId, String content, String email) {
        logger.debug("Adding comment to task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public TaskAttachment uploadTaskAttachment(Long taskId, MultipartFile file, String email) {
        logger.debug("Uploading attachment to task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            TaskAttachment attachment = new TaskAttachment();
            attachment.setTask(task);
            attachment.setUser(user);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFilePath(targetLocation.toString());
            attachment.setFileType(file.getContentType());
            attachment.setFileSize((int) file.getSize());
            attachment.setUploadDate(LocalDateTime.now());
            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            logger.error("Failed to store file for task ID: {}: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public List<TaskAttachment> getTaskAttachments(Long taskId, String email) {
        logger.debug("Fetching attachments for task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        return attachmentRepository.findByTask(task);
    }
}