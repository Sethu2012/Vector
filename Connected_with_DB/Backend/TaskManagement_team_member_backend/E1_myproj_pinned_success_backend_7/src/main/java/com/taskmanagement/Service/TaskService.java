package com.taskmanagement.Service;

import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskAttachment;
import com.taskmanagement.model.User;
import com.taskmanagement.model.Team;
import com.taskmanagement.model.TeamMember;
import com.taskmanagement.Repository.TaskRepository;
import com.taskmanagement.Repository.UserRepository;
import com.taskmanagement.Repository.CommentRepository;
import com.taskmanagement.Repository.TaskAttachmentRepository;
import com.taskmanagement.Repository.ProjectRepository;
import com.taskmanagement.Repository.TeamRepository;
import com.taskmanagement.Repository.TeamMemberRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
        List<Map<String, Object>> taskMaps = tasks.stream().map(task -> {
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
            logger.debug("Task map for task {}: {}", task.getTaskId(), taskMap);
            return taskMap;
        }).collect(Collectors.toList());
        logger.info("Returning tasks for project {}: {}", projectId, taskMaps);
        return taskMaps;
    }

    public void updateTaskCompletion(Long taskId, Integer completionPercentage, String email) {
        logger.debug("Updating task completion for task ID: {} by user: {} with percentage: {}", taskId, email, completionPercentage);
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
        } else if (completionPercentage == 100) {
            status = Task.TaskStatus.Completed;
        } else {
            status = Task.TaskStatus.On_Progress;
        }
        logger.info("Task ID: {} - Setting status to: {} for completion percentage: {}", taskId, status, completionPercentage);
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        task.setLastStatusChange(LocalDateTime.now());

        try {
            taskRepository.save(task);
            logger.info("Task completion updated successfully for task ID: {}", taskId);

            Project project = task.getCategory().getTeam().getProject();
            List<Task> projectTasks = taskRepository.findByCategoryTeamProject(project);
            if (!projectTasks.isEmpty()) {
                int totalCompletion = projectTasks.stream()
                        .mapToInt(t -> t.getCompletionPercentage() != null ? t.getCompletionPercentage() : 0)
                        .sum();
                int newProjectCompletion = totalCompletion / projectTasks.size();
                project.setCompletionPercentage(newProjectCompletion);
                if (newProjectCompletion == 100) {
                    project.setStatus(Project.ProjectStatus.Completed);
                } else if (newProjectCompletion > 0) {
                    project.setStatus(Project.ProjectStatus.In_Progress);
                } else {
                    project.setStatus(Project.ProjectStatus.Not_Started);
                }
                logger.info("Saving project {} with completion: {}", project.getProjectId(), newProjectCompletion);
                projectRepository.save(project);
                logger.info("Project ID: {} completion updated to: {}", project.getProjectId(), newProjectCompletion);
            }
        } catch (Exception e) {
            logger.error("Failed to save task ID: {} with status: {} - Error: {}", taskId, status, e.getMessage(), e);
            throw new RuntimeException("Failed to save task or update project: " + e.getMessage(), e);
        }
    }

    public void updateTaskPinnedStatus(Long taskId, Boolean isPinned, String email) {
        logger.debug("Updating pinned status for task ID: {} by user: {} to: {}", taskId, email, isPinned);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        if (!task.getAssignee().getUserId().equals(user.getUserId())) {
            logger.warn("Unauthorized attempt to pin task ID: {} by user: {}", taskId, email);
            throw new RuntimeException("Unauthorized: User is not the assignee of this task");
        }

        task.setIsPinned(isPinned != null ? isPinned : false);
        task.setUpdatedAt(LocalDateTime.now());
        try {
            taskRepository.save(task);
            logger.info("Pinned status updated successfully for task ID: {} to: {}", taskId, isPinned);
        } catch (Exception e) {
            logger.error("Failed to save pinned status for task ID: {}: {}", taskId, e.getMessage(), e);
            throw new RuntimeException("Failed to save pinned status: " + e.getMessage(), e);
        }
    }

    public List<Comment> getTaskComments(Long taskId, String email) {
        logger.debug("Fetching comments for task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        List<Comment> comments = commentRepository.findByTask(task);
        logger.info("Comments fetched for task {}: {}", taskId, comments);
        return comments;
    }

    public Comment addTaskComment(Long taskId, String content, String email, String mentionsJson) {
        logger.debug("Adding comment to task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(content);
        comment.setMentions(mentionsJson);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment added to task {}: {}", taskId, savedComment);
        return savedComment;
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
            TaskAttachment savedAttachment = attachmentRepository.save(attachment);
            logger.info("Attachment uploaded to task {}: {}", taskId, savedAttachment);
            return savedAttachment;
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
        List<TaskAttachment> attachments = attachmentRepository.findByTask(task);
        logger.info("Attachments fetched for task {}: {}", taskId, attachments);
        return attachments;
    }

    public List<Map<String, Object>> getTeamMembersForProject(Long projectId, String email) {
        logger.debug("Fetching team members for project ID: {} by user: {}", projectId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        List<Team> teams = teamRepository.findByProject(project);
        List<Map<String, Object>> teamMembers = new ArrayList<>();
        for (Team team : teams) {
            List<TeamMember> members = teamMemberRepository.findByTeam(team);
            for (TeamMember member : members) {
                User teamMemberUser = member.getUser();
                Map<String, Object> memberMap = new HashMap<>();
                memberMap.put("userId", teamMemberUser.getUserId());
                memberMap.put("fullName", teamMemberUser.getFirstName() + " " + teamMemberUser.getLastName());
                teamMembers.add(memberMap);
            }
        }
        logger.info("Team members fetched for project {}: {}", projectId, teamMembers);
        return teamMembers;
    }

    public List<String> getTaskTeamMembers(Long taskId, String email) {
        logger.debug("Fetching team members for task ID: {} by user: {}", taskId, email);
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        Project project = task.getCategory().getTeam().getProject();
        List<Team> teams = teamRepository.findByProject(project);
        List<String> teamMemberNames = teams.stream()
                .flatMap(team -> teamMemberRepository.findByTeam(team).stream())
                .map(tm -> tm.getUser().getFirstName() + " " + tm.getUser().getLastName())
                .distinct()
                .collect(Collectors.toList());
        String assignedByName = project.getCreatedBy().getFirstName() + " " + project.getCreatedBy().getLastName();
        if (!teamMemberNames.contains(assignedByName)) {
            teamMemberNames.add(assignedByName);
        }
        logger.info("Team members fetched for task {}: {}", taskId, teamMemberNames);
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