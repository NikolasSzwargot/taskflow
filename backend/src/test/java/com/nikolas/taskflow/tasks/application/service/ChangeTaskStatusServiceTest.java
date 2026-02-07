package com.nikolas.taskflow.tasks.application.service;

import com.nikolas.taskflow.tasks.adapters.persistence.JpaTaskRepositoryAdapter;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Change Task Status Service Tests")
class ChangeTaskStatusServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter;

    private ChangeTaskStatusService changeTaskStatusService;

    @BeforeEach
    void setUp() {
        changeTaskStatusService = new ChangeTaskStatusService(taskRepositoryPort, jpaTaskRepositoryAdapter);
    }

    @Test
    @DisplayName("Should change task status and update position successfully")
    void shouldChangeTaskStatusSuccessfully() {

        UUID taskId = UUID.randomUUID();
        UUID newStatusId = UUID.randomUUID();
        int maxPosition = 3;

        when(jpaTaskRepositoryAdapter.findMaxPositionByStatusId(newStatusId)).thenReturn(maxPosition);

        changeTaskStatusService.change(taskId, newStatusId);

        verify(jpaTaskRepositoryAdapter).findMaxPositionByStatusId(newStatusId);
        verify(taskRepositoryPort).updateStatusAndPosition(taskId, newStatusId, maxPosition + 1);
    }

    @Test
    @DisplayName("Should set position to 1 when moving task to empty status")
    void shouldSetPositionToOneWhenNoTasksInNewStatus() {

        UUID taskId = UUID.randomUUID();
        UUID newStatusId = UUID.randomUUID();

        when(jpaTaskRepositoryAdapter.findMaxPositionByStatusId(newStatusId)).thenReturn(0);

        changeTaskStatusService.change(taskId, newStatusId);

        verify(jpaTaskRepositoryAdapter).findMaxPositionByStatusId(newStatusId);
        verify(taskRepositoryPort).updateStatusAndPosition(taskId, newStatusId, 1);
    }
}
