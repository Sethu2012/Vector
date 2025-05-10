// Task.java
package com.taskmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private ProjectMilestone milestone;
    
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;
    
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    
    @Column(nullable = false)
    private String taskName;
    
    @Column(nullable = false)
    private String taskCode;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Low', 'Medium', 'High', 'Urgent') DEFAULT 'Medium'")
    private Priority priority = Priority.Medium;
    
    @Column(precision = 6, scale = 2)
    private BigDecimal estimatedHours;
    
    @Column(precision = 6, scale = 2)
    private BigDecimal actualHours;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Not Yet Started', 'On Progress', 'Under Review', 'Completed', 'On Hold', 'Blocked') DEFAULT 'Not Yet Started'")
    private TaskStatus status = TaskStatus.Not_Yet_Started;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer completionPercentage = 0;
    
    @Column(columnDefinition = "JSON")
    private String dependencies;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime completedAt;
    
    private LocalDateTime lastStatusChange;
    
    private Boolean isPinned;
    
    public enum Priority {
        Low, Medium, High, Urgent
    }
    
    public enum TaskStatus {
        Not_Yet_Started, On_Progress, Under_Review, Completed, On_Hold, Blocked
    }

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ProjectMilestone getMilestone() {
		return milestone;
	}

	public void setMilestone(ProjectMilestone milestone) {
		this.milestone = milestone;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
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

	public BigDecimal getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(BigDecimal estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public BigDecimal getActualHours() {
		return actualHours;
	}

	public void setActualHours(BigDecimal actualHours) {
		this.actualHours = actualHours;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Integer getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(Integer completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public String getDependencies() {
		return dependencies;
	}

	public void setDependencies(String dependencies) {
		this.dependencies = dependencies;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
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

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public LocalDateTime getLastStatusChange() {
		return lastStatusChange;
	}

	public void setLastStatusChange(LocalDateTime lastStatusChange) {
		this.lastStatusChange = lastStatusChange;
	}

	public Boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}
    
}
