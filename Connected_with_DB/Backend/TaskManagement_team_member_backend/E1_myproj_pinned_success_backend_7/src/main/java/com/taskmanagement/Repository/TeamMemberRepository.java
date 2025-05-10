package com.taskmanagement.Repository;

import com.taskmanagement.model.Team;
import com.taskmanagement.model.TeamMember;
import com.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);
}