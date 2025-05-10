package com.project2_taskmanagement.Model;

import java.time.LocalDate;


// DTO classes to match the backend response
public class HomeDataResponse {
    private String userName;
    private int myTasksCount;
    private int dueTodayCount;
    private int projectsCount;
    private int overdueCount;
    private PersonalTaskDTO[] recentTasks;
    private PersonalTaskDTO[] dueTodayTasks;
    private PersonalTaskDTO[] overdueTasks;

    // Getters and setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public int getMyTasksCount() { return myTasksCount; }
    public void setMyTasksCount(int myTasksCount) { this.myTasksCount = myTasksCount; }
    public int getDueTodayCount() { return dueTodayCount; }
    public void setDueTodayCount(int dueTodayCount) { this.dueTodayCount = dueTodayCount; }
    public int getProjectsCount() { return projectsCount; }
    public void setProjectsCount(int projectsCount) { this.projectsCount = projectsCount; }
    public int getOverdueCount() { return overdueCount; }
    public void setOverdueCount(int overdueCount) { this.overdueCount = overdueCount; }
    public PersonalTaskDTO[] getRecentTasks() { return recentTasks; }
    public void setRecentTasks(PersonalTaskDTO[] recentTasks) { this.recentTasks = recentTasks; }
    public PersonalTaskDTO[] getDueTodayTasks() { return dueTodayTasks; }
    public void setDueTodayTasks(PersonalTaskDTO[] dueTodayTasks) { this.dueTodayTasks = dueTodayTasks; }
    public PersonalTaskDTO[] getOverdueTasks() { return overdueTasks; }
    public void setOverdueTasks(PersonalTaskDTO[] overdueTasks) { this.overdueTasks = overdueTasks; }
}
