package com.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanagement.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Additional custom queries can be added here if needed.
}
