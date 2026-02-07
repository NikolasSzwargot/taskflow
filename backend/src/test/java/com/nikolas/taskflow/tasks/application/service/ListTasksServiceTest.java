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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("List Tasks Service Tests")
class ListTasksServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private ListTasksService listTasksService;

    @Test
    @DisplayName("Should return all tasks ordered by status and position")
    void shouldReturnAllTasksOrdered() {

        UUID statusId = UUID.randomUUID();
        Instant now = Instant.now();

        Task task1 = new Task(UUID.randomUUID(), "Task 1", "Description 1", statusId, 1, now, now);
        Task task2 = new Task(UUID.randomUUID(), "Task 2", "Description 2", statusId, 2, now, now);
        Task task3 = new Task(UUID.randomUUID(), "Task 3", "Description 3", statusId, 3, now, now);

        List<Task> expectedTasks = Arrays.asList(task1, task2, task3);
        when(taskRepositoryPort.findAllOrdered()).thenReturn(expectedTasks);

        List<Task> result = listTasksService.list();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepositoryPort).findAllOrdered();
    }

    @Test
    @DisplayName("Should return empty list when no tasks exist")
    void shouldReturnEmptyListWhenNoTasksExist() {

        when(taskRepositoryPort.findAllOrdered()).thenReturn(Collections.emptyList());

        List<Task> result = listTasksService.list();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepositoryPort).findAllOrdered();
    }
}
