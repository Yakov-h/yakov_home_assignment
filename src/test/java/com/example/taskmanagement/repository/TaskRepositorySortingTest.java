package com.example.taskmanagement.repository;

import com.example.taskmanagement.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositorySortingTest {

    @Autowired
    private TaskRepository repository;

    @Test
    void findPendingOrderByPriorityAndCreatedAt_sortsHighFirst_thenOldestFirst() {
        // PENDING HIGH (newer)
        TaskEntity highNew = new TaskEntity("H-new", "d", Priority.HIGH);
        ReflectionTestUtils.setField(highNew, "status", Status.PENDING);
        ReflectionTestUtils.setField(highNew, "createdAt", Instant.parse("2026-02-03T10:00:00Z"));
        repository.saveAndFlush(highNew);

        // PENDING HIGH (older) -> should come before highNew
        TaskEntity highOld = new TaskEntity("H-old", "d", Priority.HIGH);
        ReflectionTestUtils.setField(highOld, "status", Status.PENDING);
        ReflectionTestUtils.setField(highOld, "createdAt", Instant.parse("2026-02-03T09:00:00Z"));
        repository.saveAndFlush(highOld);

        // PENDING LOW
        TaskEntity low = new TaskEntity("L", "d", Priority.LOW);
        ReflectionTestUtils.setField(low, "status", Status.PENDING);
        ReflectionTestUtils.setField(low, "createdAt", Instant.parse("2026-02-03T07:00:00Z"));
        repository.saveAndFlush(low);

        // PENDING MEDIUM
        TaskEntity med = new TaskEntity("M", "d", Priority.MEDIUM);
        ReflectionTestUtils.setField(med, "status", Status.PENDING);
        ReflectionTestUtils.setField(med, "createdAt", Instant.parse("2026-02-03T08:00:00Z"));
        repository.saveAndFlush(med);

        // DONE HIGH should NOT appear at all
        TaskEntity completedHigh = new TaskEntity("CH", "d", Priority.HIGH);
        ReflectionTestUtils.setField(completedHigh, "status", Status.DONE);
        ReflectionTestUtils.setField(completedHigh, "createdAt", Instant.parse("2026-02-03T06:00:00Z"));
        repository.saveAndFlush(completedHigh);

        // When
        List<TaskEntity> result = repository.findPendingOrderByPriorityAndCreatedAt();

        // Then (expected order: HIGH oldest→newest, then MEDIUM, then LOW)
        assertThat(result).extracting(TaskEntity::getTitle)
                .containsExactly("H-old", "H-new", "M", "L");

        // Also verify only PENDING
        assertThat(result).allMatch(t -> t.getStatus() == Status.PENDING);
    }
}
