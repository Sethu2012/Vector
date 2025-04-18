// SkillSubcategoryMapping.java
package com.taskmanagement.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Data
@Entity
@Table(name = "skill_subcategory_mapping", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"skill_id", "subcategory_id"})
})
public class SkillSubcategoryMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mappingId;
    
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    
    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SkillSubcategory subcategory;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}