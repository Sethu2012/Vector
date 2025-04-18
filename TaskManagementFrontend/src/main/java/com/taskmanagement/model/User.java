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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String workEmail;
    
    @Column(nullable = false)
    private String passwordHash;
    
    private String profileImagePath;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Active', 'Inactive', 'Suspended') DEFAULT 'Active'")
    private UserStatus status = UserStatus.Active;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Available', 'Unavailable', 'On Leave') DEFAULT 'Available'")
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.Available;
    
    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'UTC'")
    private String timeZone = "UTC";
    
    @Column(columnDefinition = "JSON")
    private String preferredWorkingHours;
    
    private String contactNumber;
    
    private String department;
    
    private String workLocation;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastLogin;
    
    public enum UserStatus {
        Active, Inactive, Suspended
    }
    
    public enum AvailabilityStatus {
        Available, Unavailable, On_Leave
    }
}