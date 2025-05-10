package com.taskmanagement.Repository;

import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.Task.TaskStatus;
import com.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByAssignee(User assignee);
    long countByAssigneeAndDueDate(User assignee, LocalDate dueDate);
    long countByAssigneeAndDueDateBeforeAndStatusNot(User assignee, LocalDate dueDate, TaskStatus status);
    List<Task> findByAssigneeAndCreatedAtAfter(User assignee, LocalDateTime date);
    List<Task> findByAssigneeAndDueDate(User assignee, LocalDate dueDate);
    List<Task> findByAssigneeAndDueDateBeforeAndStatusNot(User assignee, LocalDate dueDate, TaskStatus status);
    List<Task> findByCategoryTeamProject(Project project);
    List<Task> findByCategoryTeamProjectAndAssignee(Project project, User assignee);
    List<Task> findByAssigneeAndIsPinnedTrue(User assignee);
}