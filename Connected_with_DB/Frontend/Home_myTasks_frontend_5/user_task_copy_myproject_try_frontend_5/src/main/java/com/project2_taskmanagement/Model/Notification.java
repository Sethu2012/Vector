package com.project2_taskmanagement.Model;

import java.time.LocalDateTime;

public class Notification {
    private Long notificationId;
    private Long userId; // Replaced User object with userId
    private Long senderId; // Replaced User object with senderId
    private Long relatedTaskId; // Replaced Task object with relatedTaskId
    private Long relatedProjectId; // Replaced Project object with relatedProjectId
    private Long relatedCommentId; // Replaced Comment object with relatedCommentId
    private NotificationType type;
    private String title;
    private String message;
    private NotificationImportance importance;
    private String actionUrl;
    private Boolean isRead;
    private Boolean isEmailSent;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private LocalDateTime expiresAt;

    // Default constructor (required for Jackson deserialization)
    public Notification() {}

    // Getters and setters
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRelatedTaskId() {
        return relatedTaskId;
    }

    public void setRelatedTaskId(Long relatedTaskId) {
        this.relatedTaskId = relatedTaskId;
    }

    public Long getRelatedProjectId() {
        return relatedProjectId;
    }

    public void setRelatedProjectId(Long relatedProjectId) {
        this.relatedProjectId = relatedProjectId;
    }

    public Long getRelatedCommentId() {
        return relatedCommentId;
    }

    public void setRelatedCommentId(Long relatedCommentId) {
        this.relatedCommentId = relatedCommentId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationImportance getImportance() {
        return importance;
    }

    public void setImportance(NotificationImportance importance) {
        this.importance = importance;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(Boolean isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public enum NotificationType {
        Task_Assignment, Deadline_Reminder, Comment, Status_Update,
        Role_Change, System, Mention, Approval, Performance_Review
    }

    public enum NotificationImportance {
        Low, Normal, High, Urgent
    }
}