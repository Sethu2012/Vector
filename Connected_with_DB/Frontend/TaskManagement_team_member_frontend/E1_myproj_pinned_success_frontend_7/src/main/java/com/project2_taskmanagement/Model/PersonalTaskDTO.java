package com.project2_taskmanagement.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonalTaskDTO {

    private Long personalTaskId;
    private Long userId;
    private String taskName;
    private String taskCode;
    private String description;
    private String priority;
    private boolean isCompleted;
    private LocalDateTime completionDate;
    private LocalDate taskdate;
    private String complexity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long completedBy;
    private boolean isPinned;

    // Default constructor
    public PersonalTaskDTO() {
    }

    // Constructor for mapping from backend PersonalTask
    public PersonalTaskDTO(Long personalTaskId, Long userId, String taskName, String taskCode, String description,
                          String priority, boolean isCompleted, LocalDateTime completionDate, LocalDate taskdate,
                          String complexity, LocalDateTime createdAt, LocalDateTime updatedAt, Long completedBy,
                          boolean isPinned) {
        this.personalTaskId = personalTaskId;
        this.userId = userId;
        this.taskName = taskName;
        this.taskCode = taskCode;
        this.description = description;
        this.priority = priority;
        this.isCompleted = isCompleted;
        this.completionDate = completionDate;
        this.taskdate = taskdate;
        this.complexity = complexity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedBy = completedBy;
        this.isPinned = isPinned;
    }

    // Getters and Setters
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
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

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
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

    public boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }
}