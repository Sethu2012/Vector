package com.taskmanagement.Repository;

import com.taskmanagement.model.PersonalTask;
import com.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PersonalTaskRepository extends JpaRepository<PersonalTask, Long> {
    long countByUser(User user);
    long countByUserAndTaskDate(User user, LocalDate date);
    long countByUserAndTaskDateBeforeAndIsCompletedFalse(User user, LocalDate date);
    List<PersonalTask> findByUserAndCreatedAtAfter(User user, LocalDateTime date);
    List<PersonalTask> findByUserAndTaskDate(User user, LocalDate date);
    List<PersonalTask> findByUserAndTaskDateBeforeAndIsCompletedFalse(User user, LocalDate date);
    List<PersonalTask> findByUser(User user);
    boolean existsByTaskCode(String taskCode);
    List<PersonalTask> findByUserAndIsPinnedTrue(User user);
}