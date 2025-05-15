package com.taskmanagement.Service;

import com.taskmanagement.Repository.CommentRepository; // Added import
import com.taskmanagement.Repository.ProjectRepository;
import com.taskmanagement.Repository.TaskRepository;
import com.taskmanagement.Repository.TeamMemberRepository;
import com.taskmanagement.Repository.TeamRepository;
import com.taskmanagement.Repository.UserRepository;
import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.Team;
import com.taskmanagement.model.TeamMember;
import com.taskmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository; // Added autowiring

    public List<Map<String, Object>> getProjectsForUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(new Supplier<RuntimeException>() {
                    @Override
                    public RuntimeException get() {
                        return new RuntimeException("User not found with email: " + email);
                    }
                });

        List<Project> projects = projectRepository.findProjectsForUser(user);
        if (projects.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> projectData = new ArrayList<>();
        for (Project project : projects) {
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("projectId", project.getProjectId());
            projectMap.put("projectName", project.getProjectName());
            projectMap.put("projectCode", project.getProjectCode());
            projectMap.put("description", project.getDescription());
            projectMap.put("clientName", project.getClientName());
            projectMap.put("budget", project.getBudget());
            projectMap.put("startDate", project.getStartDate());
            projectMap.put("deadline", project.getDeadline());
            projectMap.put("extendedDeadline", project.getExtendedDeadline());
            projectMap.put("extensionReason", project.getExtensionReason());
            projectMap.put("priority", project.getPriority().toString());
            projectMap.put("status", project.getStatus().toString());
            projectMap.put("completionPercentage", project.getCompletionPercentage());
            projectMap.put("riskLevel", project.getRiskLevel().toString());
            projectMap.put("createdAt", project.getCreatedAt());
            projectMap.put("updatedAt", project.getUpdatedAt());
            projectMap.put("completedAt", project.getCompletedAt());
            projectMap.put("isPinned", project.getIsPinned());
            projectMap.put("assignedBy", project.getCreatedBy().getFirstName() + " " + project.getCreatedBy().getLastName());

            List<Team> teams = teamRepository.findByProject(project);
            List<Map<String, Object>> teamData = new ArrayList<>();
            for (Team team : teams) {
                Map<String, Object> teamMap = new HashMap<>();
                teamMap.put("teamId", team.getTeamId());
                teamMap.put("teamName", team.getTeamName());
                teamMap.put("teamCode", team.getTeamCode());
                teamMap.put("description", team.getDescription());
                teamMap.put("capacity", team.getCapacity());
                teamMap.put("status", team.getStatus().toString());

                List<TeamMember> teamMembers = teamMemberRepository.findByTeam(team);
                List<String> memberNames = teamMembers.stream()
                        .map(tm -> tm.getUser().getFirstName() + " " + tm.getUser().getLastName())
                        .collect(Collectors.toList());
                teamMap.put("members", memberNames);

                teamData.add(teamMap);
            }
            projectMap.put("teams", teamData);

            List<Task> tasks = taskRepository.findByCategoryTeamProjectAndAssignee(project, user);
            List<Map<String, Object>> taskData = tasks.stream().map(task -> {
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
                taskMap.put("isPinned", task.getIsPinned() != null ? task.getIsPinned() : false);
                return taskMap;
            }).collect(Collectors.toList());
            projectMap.put("tasks", taskData);

            projectData.add(projectMap);
        }

        return projectData;
    }

    public Map<String, Object> getProjectDetails(Long projectId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(new Supplier<RuntimeException>() {
                    @Override
                    public RuntimeException get() {
                        return new RuntimeException("User not found with email: " + email);
                    }
                });

        Project project = projectRepository.findById(projectId)
                .orElseThrow(new Supplier<RuntimeException>() {
                    @Override
                    public RuntimeException get() {
                        return new RuntimeException("Project not found with ID: " + projectId);
                    }
                });

        boolean isManager = project.getManager().getUserId().equals(user.getUserId());
        boolean isTeamMember = false;
        List<Team> teams = teamRepository.findByProject(project);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberRepository.findByTeam(team);
            if (members.stream().anyMatch(tm -> tm.getUser().getUserId().equals(user.getUserId()))) {
                isTeamMember = true;
                break;
            }
        }

        if (!isManager && !isTeamMember) {
            throw new RuntimeException("Unauthorized: User is neither the manager nor a team member of project ID " + projectId);
        }

        Map<String, Object> projectMap = new HashMap<>();
        projectMap.put("projectId", project.getProjectId());
        projectMap.put("projectName", project.getProjectName());
        projectMap.put("projectCode", project.getProjectCode());
        projectMap.put("description", project.getDescription());
        projectMap.put("clientName", project.getClientName());
        projectMap.put("budget", project.getBudget());
        projectMap.put("startDate", project.getStartDate());
        projectMap.put("deadline", project.getDeadline());
        projectMap.put("extendedDeadline", project.getExtendedDeadline());
        projectMap.put("extensionReason", project.getExtensionReason());
        projectMap.put("priority", project.getPriority().toString());
        projectMap.put("status", project.getStatus().toString());
        projectMap.put("completionPercentage", project.getCompletionPercentage());
        projectMap.put("riskLevel", project.getRiskLevel().toString());
        projectMap.put("createdAt", project.getCreatedAt());
        projectMap.put("updatedAt", project.getUpdatedAt());
        projectMap.put("completedAt", project.getCompletedAt());
        projectMap.put("isPinned", project.getIsPinned());
        projectMap.put("assignedBy", project.getCreatedBy().getFirstName() + " " + project.getCreatedBy().getLastName());

        List<Team> projectTeams = teamRepository.findByProject(project);
        List<Map<String, Object>> teamData = new ArrayList<>();
        for (Team team : projectTeams) {
            Map<String, Object> teamMap = new HashMap<>();
            teamMap.put("teamId", team.getTeamId());
            teamMap.put("teamName", team.getTeamName());
            teamMap.put("teamCode", team.getTeamCode());
            teamMap.put("description", team.getDescription());
            teamMap.put("capacity", team.getCapacity());
            teamMap.put("status", team.getStatus().toString());

            List<TeamMember> teamMembers = teamMemberRepository.findByTeam(team);
            List<String> memberNames = teamMembers.stream()
                    .map(tm -> tm.getUser().getFirstName() + " " + tm.getUser().getLastName())
                    .collect(Collectors.toList());
            teamMap.put("members", memberNames);

            teamData.add(teamMap);
        }
        projectMap.put("teams", teamData);

        List<Task> tasks = taskRepository.findByCategoryTeamProject(project);
        List<Map<String, Object>> taskData = tasks.stream().map(task -> {
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
            taskMap.put("isPinned", task.getIsPinned() != null ? task.getIsPinned() : false);
            return taskMap;
        }).collect(Collectors.toList());
        projectMap.put("tasks", taskData);

        return projectMap;
    }

    public List<Comment> getProjectComments(Long projectId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByWorkEmail(email)
                .orElseThrow(new Supplier<RuntimeException>() {
                    @Override
                    public RuntimeException get() {
                        return new RuntimeException("User not found with email: " + email);
                    }
                });

        Project project = projectRepository.findById(projectId)
                .orElseThrow(new Supplier<RuntimeException>() {
                    @Override
                    public RuntimeException get() {
                        return new RuntimeException("Project not found with ID: " + projectId);
                    }
                });

        boolean isManager = project.getManager().getUserId().equals(user.getUserId());
        boolean isTeamMember = false;
        List<Team> teams = teamRepository.findByProject(project);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberRepository.findByTeam(team);
            if (members.stream().anyMatch(tm -> tm.getUser().getUserId().equals(user.getUserId()))) {
                isTeamMember = true;
                break;
            }
        }

        if (!isManager && !isTeamMember) {
            throw new RuntimeException("Unauthorized: User is neither the manager nor a team member of project ID " + projectId);
        }

        List<Task> tasks = taskRepository.findByCategoryTeamProject(project);
        List<Comment> allComments = new ArrayList<>();
        for (Task task : tasks) {
            List<Comment> taskComments = commentRepository.findByTaskWithUser(task); // Replaced task.getComments()
            allComments.addAll(taskComments); // Simplified since findByTaskWithUser returns a list (empty if no comments)
        }

        return allComments.stream()
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .collect(Collectors.toList());
    }
}