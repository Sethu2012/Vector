package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taskmanagement.model.PersonalTask.Complexity;
import com.taskmanagement.model.PersonalTask.Priority;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonalTaskDTO {
    private Long personalTaskId;
    private Long userId;

    @NotNull(message = "Task name is required")
    private String taskName;

    @NotNull(message = "Task code is required")
    private String taskCode;

    private String description;
    private Priority priority;
    private Boolean isCompleted;
    private LocalDateTime completionDate;
    private LocalDate taskdate;
    private Complexity complexity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long completedBy;
    private Boolean isPinned;

    // Constructor to map from PersonalTask entity
    public PersonalTaskDTO(PersonalTask task) {
        this.personalTaskId = task.getPersonalTaskId();
        this.userId = task.getUser().getUserId();
        this.taskName = task.getTaskName();
        this.taskCode = task.getTaskCode();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.isCompleted = task.getIsCompleted();
        this.completionDate = task.getCompletionDate();
        this.taskdate = task.getTaskDate();
        this.complexity = task.getComplexity();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.completedBy = task.getCompletedBy() != null ? task.getCompletedBy().getUserId() : null;
        this.isPinned = task.getIsPinned();
    }

    // Default constructor
    public PersonalTaskDTO() {
    }

    // Custom deserialization for priority and complexity
    @JsonCreator
    public PersonalTaskDTO(
        @JsonProperty("personalTaskId") Long personalTaskId,
        @JsonProperty("userId") Long userId,
        @JsonProperty("taskName") String taskName,
        @JsonProperty("taskCode") String taskCode,
        @JsonProperty("description") String description,
        @JsonProperty("priority") String priority,
        @JsonProperty("isCompleted") Boolean isCompleted,
        @JsonProperty("completionDate") LocalDateTime completionDate,
        @JsonProperty("taskdate") LocalDate taskdate,
        @JsonProperty("complexity") String complexity,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @JsonProperty("updatedAt") LocalDateTime updatedAt,
        @JsonProperty("completedBy") Long completedBy,
        @JsonProperty("isPinned") Boolean isPinned
    ) {
        this.personalTaskId = personalTaskId;
        this.userId = userId;
        this.taskName = taskName;
        this.taskCode = taskCode;
        this.description = description;
        this.priority = priority != null ? Priority.valueOf(priority) : null;
        this.isCompleted = isCompleted;
        this.completionDate = completionDate;
        this.taskdate = taskdate;
        this.complexity = complexity != null ? Complexity.valueOf(complexity) : null;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedBy = completedBy;
        this.isPinned = isPinned;
    }

    // Getter for priority as String
    public String getPriority() {
        return priority != null ? priority.name() : null;
    }

    // Setter for priority as String
    public void setPriority(String priority) {
        this.priority = priority != null ? Priority.valueOf(priority) : null;
    }

    // Getter for complexity as String
    public String getComplexity() {
        return complexity != null ? complexity.name() : null;
    }

    // Setter for complexity as String
    public void setComplexity(String complexity) {
        this.complexity = complexity != null ? Complexity.valueOf(complexity) : null;
    }

    // Other getters and setters
    public Long getPersonalTaskId() {
        return personalTaskId;
    }

    public void setPersonalTaskId(Long personalTaskId) {
        this.personalTaskId = personalTaskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public LocalDate getTaskdate() {
        return taskdate;
    }

    public void setTaskdate(LocalDate taskdate) {
        this.taskdate = taskdate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(Long completedBy) {
        this.completedBy = completedBy;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }
}