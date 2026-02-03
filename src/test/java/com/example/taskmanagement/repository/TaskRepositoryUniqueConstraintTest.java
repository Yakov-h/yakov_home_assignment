package com.example.taskmanagement.repository;

import com.example.taskmanagement.domain.Priority;
import com.example.taskmanagement.domain.TaskEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryUniqueConstraintTest {

    @Autowired
    private TaskRepository repository;

    @Test
    void sameTitle_sameUtcDay_shouldFail() {
        TaskEntity t1 = new TaskEntity("Buy milk", "x", Priority.HIGH);
        ReflectionTestUtils.setField(t1, "createdAt", Instant.parse("2026-02-03T10:00:00Z"));
        repository.saveAndFlush(t1);

        TaskEntity t2 = new TaskEntity("Buy milk", "y", Priority.LOW);
        ReflectionTestUtils.setField(t2, "createdAt", Instant.parse("2026-02-03T18:00:00Z"));

        assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(t2));
    }

    @Test
    void sameTitle_differentUtcDay_shouldPass() {
        TaskEntity t1 = new TaskEntity("Buy milk", "x", Priority.HIGH);
        ReflectionTestUtils.setField(t1, "createdAt", Instant.parse("2026-02-03T10:00:00Z"));
        repository.saveAndFlush(t1);

        TaskEntity t2 = new TaskEntity("Buy milk", "y", Priority.LOW);
        ReflectionTestUtils.setField(t2, "createdAt", Instant.parse("2026-02-04T00:01:00Z"));

        assertDoesNotThrow(() -> repository.saveAndFlush(t2));
    }

    @Test
    void differentTitle_sameUtcDay_shouldPass() {
        TaskEntity t1 = new TaskEntity("Buy milk", "x", Priority.HIGH);
        ReflectionTestUtils.setField(t1, "createdAt", Instant.parse("2026-02-03T10:00:00Z"));
        repository.saveAndFlush(t1);

        TaskEntity t2 = new TaskEntity("Buy bread", "y", Priority.LOW);
        ReflectionTestUtils.setField(t2, "createdAt", Instant.parse("2026-02-03T18:00:00Z"));

        assertDoesNotThrow(() -> repository.saveAndFlush(t2));
    }
}
