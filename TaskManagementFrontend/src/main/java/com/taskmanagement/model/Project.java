package com.taskmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Setter
@Getter
@ToString
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
    
    @Column(nullable = false)
    private String projectName;
    
    @Column(unique = true, nullable = false)
    private String projectCode;
    
    private String description;
    
    private String clientName;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal budget;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate deadline;
    
    private LocalDate extendedDeadline;
    
    private String extensionReason;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Low', 'Medium', 'High', 'Critical') DEFAULT 'Medium'")
    private Priority priority = Priority.Medium;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Not Started', 'Planning', 'In Progress', 'On Hold', 'Completed') DEFAULT 'Not Started'")
    private ProjectStatus status = ProjectStatus.Not_Started;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer completionPercentage = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Low', 'Medium', 'High') DEFAULT 'Medium'")
    private RiskLevel riskLevel = RiskLevel.Medium;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime completedAt;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    private Boolean isPinned;
    
    public enum Priority {
        Low, Medium, High, Critical
    }
    
    public enum ProjectStatus {
        Not_Started, Planning, In_Progress, On_Hold, Completed
    }
    
    public enum RiskLevel {
        Low, Medium, High
    }
}