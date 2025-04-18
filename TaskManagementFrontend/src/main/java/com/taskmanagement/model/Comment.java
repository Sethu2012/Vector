// Comment.java
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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean hasAttachment = false;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPrivate = false;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isEdited = false;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPinned = false;
    
    @Column(columnDefinition = "JSON")
    private String mentions;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}