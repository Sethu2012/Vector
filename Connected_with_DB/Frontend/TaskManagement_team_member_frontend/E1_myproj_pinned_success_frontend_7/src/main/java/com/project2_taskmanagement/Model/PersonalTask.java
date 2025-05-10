package com.project2_taskmanagement.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonalTask {
    private Long personalTaskId;
    private Long userId; // Replaced User object with userId
    private String taskName;
    private String taskCode;
    private String description;
    private Priority priority;
    private Boolean isCompleted;
    private LocalDateTime completionDate;
    private LocalDate taskDate;
    private Complexity complexity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long completedBy; // Replaced User object with completedBy ID
    private Boolean isPinned;

    // Default constructor (required for Jackson deserialization)
    public PersonalTask() {}

    // Getters and setters
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
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

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public enum Priority {
        Low, Medium, High, Urgent
    }

    public enum Complexity {
        Simple, Moderate, Complex
    }
}