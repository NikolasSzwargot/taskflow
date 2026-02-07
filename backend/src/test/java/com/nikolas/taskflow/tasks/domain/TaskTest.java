package com.nikolas.taskflow.tasks.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Domain Model Tests")
class TaskTest {

    @Test
    @DisplayName("Should create task with all fields correctly initialized")
    void shouldCreateTaskWithAllFields() {

        UUID id = UUID.randomUUID();
        String title = "Test Task";
        String description = "Test Description";
        UUID statusId = UUID.randomUUID();
        int position = 1;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        Task task = new Task(id, title, description, statusId, position, createdAt, updatedAt);

        assertNotNull(task);
        assertEquals(id, task.id());
        assertEquals(title, task.title());
        assertEquals(description, task.description());
        assertEquals(statusId, task.statusId());
        assertEquals(position, task.position());
        assertEquals(createdAt, task.createdAt());
        assertEquals(updatedAt, task.updatedAt());
    }

    @Test
    @DisplayName("Should support equality for tasks with same values")
    void shouldSupportEqualityForSameValues() {

        UUID id = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        Instant now = Instant.now();

        Task task1 = new Task(id, "Title", "Description", statusId, 1, now, now);
        Task task2 = new Task(id, "Title", "Description", statusId, 1, now, now);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when tasks have different IDs")
    void shouldNotBeEqualForDifferentIds() {

        UUID statusId = UUID.randomUUID();
        Instant now = Instant.now();

        Task task1 = new Task(UUID.randomUUID(), "Title", "Description", statusId, 1, now, now);
        Task task2 = new Task(UUID.randomUUID(), "Title", "Description", statusId, 1, now, now);

        assertNotEquals(task1, task2);
    }
}
