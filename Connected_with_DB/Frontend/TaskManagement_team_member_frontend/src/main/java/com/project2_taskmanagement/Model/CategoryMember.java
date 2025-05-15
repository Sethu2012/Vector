package com.project2_taskmanagement.Model;

import java.time.LocalDateTime;

public class CategoryMember {
    private Long categoryMemberId;
    private Long categoryId;
    private Long userId;
    private Boolean isCategoryLead;
    private Integer allocationPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;
    private Long createdBy;

    // Default constructor (required for Jackson deserialization)
    public CategoryMember() {}

    // Getters and setters
    public Long getCategoryMemberId() {
        return categoryMemberId;
    }

    public void setCategoryMemberId(Long categoryMemberId) {
        this.categoryMemberId = categoryMemberId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsCategoryLead() {
        return isCategoryLead;
    }

    public void setIsCategoryLead(Boolean isCategoryLead) {
        this.isCategoryLead = isCategoryLead;
    }

    public Integer getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(Integer allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
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

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}