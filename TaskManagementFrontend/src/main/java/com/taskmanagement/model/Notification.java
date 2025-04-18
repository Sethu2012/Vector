// Notification.java
package com.taskmanagement.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    
    @ManyToOne
    @JoinColumn(name = "related_task_id")
    private Task relatedTask;
    
    @ManyToOne
    @JoinColumn(name = "related_project_id")
    private Project relatedProject;
    
    @ManyToOne
    @JoinColumn(name = "related_comment_id")
    private Comment relatedComment;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Low', 'Normal', 'High', 'Urgent') DEFAULT 'Normal'")
    private NotificationImportance importance = NotificationImportance.Normal;
    
    private String actionUrl;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead = false;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isEmailSent = false;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private LocalDateTime readAt;
    
    private LocalDateTime expiresAt;
    
    public enum NotificationType {
        Task_Assignment, Deadline_Reminder, Comment, Status_Update, 
        Role_Change, System, Mention, Approval, Performance_Review
    }
    
    public enum NotificationImportance {
        Low, Normal, High, Urgent
    }
}