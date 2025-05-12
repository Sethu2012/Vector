package com.taskmanagement.model;

import java.time.LocalDateTime;
import jakarta.persistence.Table;


@Table(name = "users")
public class User {

	    private Long userId;
	    private String firstName;
	    private String lastName;
	    private String workEmail;
	    private String passwordHash;
	    private String profileImagePath;
	    private Role role;
	    private String status;
	    private String availabilityStatus;
	    private String timeZone;
	    private String preferredWorkingHours;
	    private String contactNumber;
	    private String department;
	    private String workLocation;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    private LocalDateTime lastLogin;

	    public User() {}

	    public Long getUserId() { return userId; }
	    public void setUserId(Long userId) { this.userId = userId; }
	    public String getFirstName() { return firstName; }
	    public void setFirstName(String firstName) { this.firstName = firstName; }
	    public String getLastName() { return lastName; }
	    public void setLastName(String lastName) { this.lastName = lastName; }
	    public String getWorkEmail() { return workEmail; }
	    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }
	    public String getPasswordHash() { return passwordHash; }
	    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
	    public String getProfileImagePath() { return profileImagePath; }
	    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
	    public Role getRole() { return role; }
	    public void setRole(Role role) { this.role = role; }
	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }
	    public String getAvailabilityStatus() { return availabilityStatus; }
	    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
	    public String getTimeZone() { return timeZone; }
	    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }
	    public String getPreferredWorkingHours() { return preferredWorkingHours; }
	    public void setPreferredWorkingHours(String preferredWorkingHours) { this.preferredWorkingHours = preferredWorkingHours; }
	    public String getContactNumber() { return contactNumber; }
	    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
	    public String getDepartment() { return department; }
	    public void setDepartment(String department) { this.department = department; }
	    public String getWorkLocation() { return workLocation; }
	    public void setWorkLocation(String workLocation) { this.workLocation = workLocation; }
	    public LocalDateTime getCreatedAt() { return createdAt; }
	    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	    public LocalDateTime getUpdatedAt() { return updatedAt; }
	    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	    public LocalDateTime getLastLogin() { return lastLogin; }
	    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
	
}