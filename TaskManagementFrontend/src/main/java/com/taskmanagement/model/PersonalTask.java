// PersonalTask.java
package com.taskmanagement.model;

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
@Table(name = "personal_tasks")
public class PersonalTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalTaskId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String taskName;
    
    @Column(nullable = false, unique = true)
    private String taskCode;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Low', 'Medium', 'High', 'Urgent') DEFAULT 'Medium'")
    private Priority priority = Priority.Medium;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isCompleted = false;
    
    private LocalDateTime completionDate;
    
    @Column(nullable = false)
    private LocalDate taskDate;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Simple', 'Moderate', 'Complex') DEFAULT 'Moderate'")
    private Complexity complexity = Complexity.Moderate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "completed_by_user_id")
    private User completedBy;
    
    private Boolean isPinned;
    
    public enum Priority {
        Low, Medium, High, Urgent
    }
    
    public enum Complexity {
        Simple, Moderate, Complex
    }
}