package com.taskmanagement.Service;

import com.taskmanagement.model.*;
import com.taskmanagement.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
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

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TaskService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            logger.error("Could not create upload directory: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    // Authorization check to determine if a user has access to a task

    private boolean hasAccessToTask(User user, Task task) {
        logger.debug("Checking access for user {} to task {}", user.getUserId(), task.getTaskId());
        if (task.getAssignee() != null && task.getAssignee().getUserId().equals(user.getUserId())) {
            return true;
        }
        if (task.getCreator() != null && task.getCreator().getUserId().equals(user.getUserId())) {
            return true;
        }
        Project project = task.getCategory().getTeam().getProject();
        if (project.getManager() != null && project.getManager().getUserId().equals(user.getUserId())) {
            return true;
        }
        List<Team> teams = teamRepository.findByProject(project);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberRepository.findByTeam(team);
            if (members.stream().anyMatch(tm -> tm.getUser().getUserId().equals(user.getUserId()))) {
                return true;
            }
        }
        return false;
    }

    public List<Comment> getTaskComments(Long taskId, String email) {
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if (!hasAccessToTask(user, task)) {
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }
        List<Comment> comments = commentRepository.findByTaskWithUser(task);
        comments.sort(Comparator.comparing(Comment::getCreatedAt));
        return comments;
    }

    public Comment addTaskComment(Long taskId, String content, String email, String mentionsJson) {
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if (!hasAccessToTask(user, task)) {
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Comment content cannot be empty");
        }
        String validatedMentions = mentionsJson != null && !mentionsJson.trim().isEmpty() ? mentionsJson : "[]";
        try {
            objectMapper.readTree(validatedMentions);
        } catch (Exception e) {
            throw new RuntimeException("Invalid mentions format: " + e.getMessage());
        }
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(content);
        comment.setMentions(validatedMentions);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setHasAttachment(false);
        comment.setIsPrivate(false);
        comment.setIsEdited(false);
        comment.setIsPinned(false);
        comment.setParentComment(null);
        return commentRepository.save(comment);
    }
    public List<Map<String, Object>> getTasksByProject(Long projectId, String email) {
        logger.debug("Fetching tasks for project ID: {} by user: {}", projectId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        List<Team> teams = teamRepository.findByProject(project);
        List<String> teamMemberNames = teams.stream()
                .flatMap(team -> teamMemberRepository.findByTeam(team).stream())
                .map(tm -> tm.getUser().getFirstName() + " " + tm.getUser().getLastName())
                .distinct()
                .collect(Collectors.toList());
        String assignedByName = project.getCreatedBy().getFirstName() + " " + project.getCreatedBy().getLastName();
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
            taskMap.put("teamMembers", teamMemberNames);
            taskMap.put("assignedBy", assignedByName);
            taskMap.put("isPinned", task.getIsPinned() != null ? task.getIsPinned() : false);
            return taskMap;
        }).collect(Collectors.toList());
    }

    public void updateTaskCompletion(Long taskId, Integer completionPercentage, String email) {
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if (!hasAccessToTask(user, task)) {
            logger.warn("User {} denied access to update task {}", user.getUserId(), taskId);
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }
        if (completionPercentage < 0 || completionPercentage > 100) {
            throw new RuntimeException("Invalid completion percentage");
        }
        task.setCompletionPercentage(completionPercentage);
        Task.TaskStatus status = completionPercentage == 0 ? Task.TaskStatus.Not_Yet_Started :
                                 completionPercentage == 100 ? Task.TaskStatus.Completed :
                                 Task.TaskStatus.On_Progress;
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        task.setLastStatusChange(LocalDateTime.now());
        taskRepository.save(task);

        Project project = task.getCategory().getTeam().getProject();
        List<Task> projectTasks = taskRepository.findByCategoryTeamProject(project);
        if (!projectTasks.isEmpty()) {
            int totalCompletion = projectTasks.stream()
                    .mapToInt(t -> t.getCompletionPercentage() != null ? t.getCompletionPercentage() : 0)
                    .sum();
            int newProjectCompletion = totalCompletion / projectTasks.size();
            project.setCompletionPercentage(newProjectCompletion);
            project.setStatus(newProjectCompletion == 100 ? Project.ProjectStatus.Completed :
                             newProjectCompletion > 0 ? Project.ProjectStatus.In_Progress :
                             Project.ProjectStatus.Not_Started);
            projectRepository.save(project);
        }
    }

    public void updateTaskPinnedStatus(Long taskId, Boolean isPinned, String email) {
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if (!hasAccessToTask(user, task)) {
            logger.warn("User {} denied access to pin task {}", user.getUserId(), taskId);
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }
        task.setIsPinned(isPinned != null ? isPinned : false);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }
    public TaskAttachment uploadTaskAttachment(Long taskId, MultipartFile file, String email) {
        // Check if taskId is null
        if (taskId == null) {
            throw new RuntimeException("Task ID cannot be null");
        }

        // Retrieve the Task object from the repository
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        // Check user access (using the existing hasAccess method)
        if (!hasAccess(email, taskId)) {
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }

        // Check if the file is empty
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Create and populate the TaskAttachment
        TaskAttachment attachment = new TaskAttachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setTask(task); // Set the Task object instead of taskId
        // Add additional fields as needed (e.g., filePath, fileType, etc.)
        
        // Save file logic here (if implemented)
        // For example:
        // String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        // Path targetLocation = this.fileStorageLocation.resolve(fileName);
        // Files.copy(file.getInputStream(), targetLocation);
        // attachment.setFilePath(targetLocation.toString());
        
        // Save the attachment to the database (if repository is used)
        // return attachmentRepository.save(attachment);

        return attachment; // Return the attachment (adjust based on your save logic)
    }
    // Simulated method to get attachments
    public List<TaskAttachment> getTaskAttachments(Long taskId, String email) {
        if (!taskExists(taskId)) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        if (!hasAccess(email, taskId)) {
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }
        // Return attachments logic here
        return List.of(); // Placeholder
    }

    // Helper methods (placeholders)
    private boolean taskExists(Long taskId) {
        // Implement task existence check
        return true;
    }

    private boolean hasAccess(String email, Long taskId) {
        // Implement access check
        return true;
    }

    public List<String> getTaskTeamMembers(Long taskId, String email) {
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if (!hasAccessToTask(user, task)) {
            logger.warn("User {} denied access to team members for task {}", user.getUserId(), taskId);
            throw new RuntimeException("Unauthorized: User does not have access to this task");
        }
        Project project = task.getCategory().getTeam().getProject();
        List<Team> teams = teamRepository.findByProject(project);
        List<String> teamMemberNames = teams.stream()
                .flatMap(team -> teamMemberRepository.findByTeam(team).stream())
                .map(tm -> tm.getUser().getFirstName() + " " + tm.getUser().getLastName())
                .distinct()
                .collect(Collectors.toList());
        String assignedByName = task.getCreator().getFirstName() + " " + task.getCreator().getLastName();
        if (!teamMemberNames.contains(assignedByName)) {
            teamMemberNames.add(assignedByName);
        }
        return teamMemberNames;
    }

    public Map<Project, List<Task>> getPinnedProjectTasksGroupedByProject(String email) {
        User user = userRepository.findByWorkEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<Task> pinnedTasks = taskRepository.findByAssigneeAndIsPinnedTrue(user);
        return pinnedTasks.stream()
            .collect(Collectors.groupingBy(task -> task.getCategory().getTeam().getProject()));
    }
}