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
}
