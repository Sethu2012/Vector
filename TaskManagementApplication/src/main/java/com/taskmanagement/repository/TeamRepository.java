package com.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByProject_ProjectId(Long projectId);
}