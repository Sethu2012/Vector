// Team.java
package com.taskmanagement.model;

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
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @Column(nullable = false)
    private String teamName;
    
    @Column(unique = true, nullable = false)
    private String teamCode;
    
    private String description;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer capacity = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Forming', 'Active', 'Disbanded') DEFAULT 'Forming'")
    private TeamStatus status = TeamStatus.Forming;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    public enum TeamStatus {
        Forming, Active, Disbanded
    }
}