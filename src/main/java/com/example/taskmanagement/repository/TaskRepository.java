package com.example.taskmanagement.repository;

import com.example.taskmanagement.domain.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("""
        SELECT t
        FROM TaskEntity t
        WHERE t.status = 'PENDING'
        ORDER BY
            CASE t.priority
                WHEN 'HIGH' THEN 1
                WHEN 'MEDIUM' THEN 2
                WHEN 'LOW' THEN 3
            END,
            t.createdAt ASC
    """)
    List<TaskEntity> findPendingOrderByPriorityAndCreatedAt();
}
