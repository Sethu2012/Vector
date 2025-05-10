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
@Table(name = "project_milestones")
public class ProjectMilestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneId;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @Column(nullable = false)
    private String milestoneName;
    
    private String description;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Not Started', 'In Progress', 'Completed', 'Delayed') DEFAULT 'Not Started'")
    private MilestoneStatus status = MilestoneStatus.Not_Started;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer completionPercentage = 0;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime completedAt;
    
    public enum MilestoneStatus {
        Not_Started, In_Progress, Completed, Delayed
    }
}