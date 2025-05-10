// CategoryMember.java
package com.taskmanagement.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "category_members")
public class CategoryMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryMemberId;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isCategoryLead = false;
    
    @Column(columnDefinition = "INT DEFAULT 100")
    private Integer allocationPercentage = 100;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime removedAt;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}