package com.project2_taskmanagement.Model;

import java.time.LocalDateTime;

public class CommentVisibility {
    private Long visibilityId;
    private Long commentId; // Replaced Comment object with commentId
    private Long userId; // Replaced User object with userId
    private LocalDateTime createdAt;

    // Default constructor (required for Jackson deserialization)
    public CommentVisibility() {}

    // Getters and setters
    public Long getVisibilityId() {
        return visibilityId;
    }

    public void setVisibilityId(Long visibilityId) {
        this.visibilityId = visibilityId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}