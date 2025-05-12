package com.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanagement.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findByStatus(Project.ProjectStatus status);
	List<Project> findByManagerWorkEmailOrCreatedByWorkEmail(String managerEmail, String createdByEmail);
}
