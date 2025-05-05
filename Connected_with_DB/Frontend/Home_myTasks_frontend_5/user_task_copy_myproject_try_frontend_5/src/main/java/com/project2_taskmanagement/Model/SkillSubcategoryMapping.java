package com.project2_taskmanagement.Model;

import java.time.LocalDateTime;

public class SkillSubcategoryMapping {
    private Long mappingId;
    private Long skillId;
    private Long subcategoryId;
    private LocalDateTime createdAt;

    // Default constructor (required for Jackson deserialization)
    public SkillSubcategoryMapping() {}

    // Getters and setters
    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}