package com.taskmanagement.Repository;

import com.taskmanagement.model.Project;
import com.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    long countByManager(User manager);
    List<Project> findByManager(User manager);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.teams t LEFT JOIN TeamMember tm ON tm.team = t WHERE p.manager = :user OR tm.user = :user")
    List<Project> findProjectsForUser(@Param("user") User user);
}