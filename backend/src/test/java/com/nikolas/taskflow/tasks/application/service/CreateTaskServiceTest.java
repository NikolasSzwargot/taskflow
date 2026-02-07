package com.nikolas.taskflow.tasks.application.service;

import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import com.nikolas.taskflow.tasks.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Create Task Service Tests")
class CreateTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private CreateTaskService createTaskService;

    @Test
    @DisplayName("Should create task successfully with correct position")
    void shouldCreateTaskSuccessfully() {

        String title = "New Task";
        String description = "Task Description";
        UUID statusId = UUID.randomUUID();
        int maxPosition = 5;

        when(taskRepositoryPort.findMaxPositionByStatusId(statusId)).thenReturn(maxPosition);
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = createTaskService.create(title, description, statusId);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(title, result.title());
        assertEquals(description, result.description());
        assertEquals(statusId, result.statusId());
        assertEquals(maxPosition + 1, result.position());
        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());

        verify(taskRepositoryPort).findMaxPositionByStatusId(statusId);
        verify(taskRepositoryPort).save(any(Task.class));
    }

    @Test
    @DisplayName("Should create first task at position 1 when no tasks exist")
    void shouldCreateTaskAtPositionOneWhenNoTasksExist() {

        String title = "First Task";
        String description = "First Task Description";
        UUID statusId = UUID.randomUUID();

        when(taskRepositoryPort.findMaxPositionByStatusId(statusId)).thenReturn(0);
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = createTaskService.create(title, description, statusId);

        assertNotNull(result);
        assertEquals(1, result.position());
        verify(taskRepositoryPort).findMaxPositionByStatusId(statusId);
        verify(taskRepositoryPort).save(any(Task.class));
    }

    @Test
    @DisplayName("Should set createdAt and updatedAt to current time")
    void shouldSetCreatedAtAndUpdatedAtToCurrentTime() {

        String title = "New Task";
        String description = "Task Description";
        UUID statusId = UUID.randomUUID();
        Instant before = Instant.now();

        when(taskRepositoryPort.findMaxPositionByStatusId(statusId)).thenReturn(0);
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = createTaskService.create(title, description, statusId);
        Instant after = Instant.now();

        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());
        assertTrue(!result.createdAt().isBefore(before));
        assertTrue(!result.createdAt().isAfter(after));
        assertEquals(result.createdAt(), result.updatedAt());
    }
}
