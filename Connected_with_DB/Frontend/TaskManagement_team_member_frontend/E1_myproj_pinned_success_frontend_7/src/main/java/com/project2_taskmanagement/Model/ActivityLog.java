// ActivityLog.java
package com.project2_taskmanagement.Model;

import java.time.LocalDateTime;

public class ActivityLog {
   
    private Long logId;
    
    
    private User user;
    
   
    private String actionType;
    
    
    private String entityType;
    
   
    private Long entityId;
    
    private String description;
    
    private String additionalData;
    
    private Integer statusCode;
   
    private LocalDateTime createdAt;
}