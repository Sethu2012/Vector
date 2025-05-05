package com.taskmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
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
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Team> teams = new ArrayList<>();
    
    public enum Priority {
        Low, Medium, High, Critical
    }
    
    public enum ProjectStatus {
        Not_Started, Planning, In_Progress, On_Hold, Completed
    }
    
    public enum RiskLevel {
        Low, Medium, High
    }

    // Getters and Setters (provided as is from your code)
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDate getExtendedDeadline() {
        return extendedDeadline;
    }

    public void setExtendedDeadline(LocalDate extendedDeadline) {
        this.extendedDeadline = extendedDeadline;
    }

    public String getExtensionReason() {
        return extensionReason;
    }

    public void setExtensionReason(String extensionReason) {
        this.extensionReason = extensionReason;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Integer getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}