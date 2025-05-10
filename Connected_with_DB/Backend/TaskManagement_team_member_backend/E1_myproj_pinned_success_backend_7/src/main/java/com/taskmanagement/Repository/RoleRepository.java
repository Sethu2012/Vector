package com.taskmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}