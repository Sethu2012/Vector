package com.taskmanagement.Repository;

import com.taskmanagement.model.Comment;
import com.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByTask(Task task);
    List<Comment> findByTaskAndCreatedAtAfter(Task task, LocalDateTime after);
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.task = :task")
    List<Comment> findByTaskWithUser(@Param("task") Task task);    
}