// Continued from NotificationSetting.java
package com.taskmanagement.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
@Table(name = "notification_settings")
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private String notificationType;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean emailEnabled = true;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean pushEnabled = true;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean inAppEnabled = true;
    
    private LocalTime quietHoursStart;
    
    private LocalTime quietHoursEnd;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}