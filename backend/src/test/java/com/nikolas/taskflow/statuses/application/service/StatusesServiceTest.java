package com.nikolas.taskflow.statuses.application.service;

import com.nikolas.taskflow.statuses.application.ports.out.StatusRepositoryPort;
import com.nikolas.taskflow.statuses.domain.Status;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Statuses Service Tests")
class StatusesServiceTest {

    @Mock
    private StatusRepositoryPort statusRepositoryPort;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private StatusesService statusesService;

    @Test
    @DisplayName("Should return all statuses ordered by position")
    void shouldListAllStatusesOrdered() {
        Instant now = Instant.now();
        Status status1 = new Status(UUID.randomUUID(), "To Do", 1, now);
        Status status2 = new Status(UUID.randomUUID(), "In Progress", 2, now);
        Status status3 = new Status(UUID.randomUUID(), "Done", 3, now);

        List<Status> expectedStatuses = Arrays.asList(status1, status2, status3);
        when(statusRepositoryPort.findAllOrdered()).thenReturn(expectedStatuses);

        List<Status> result = statusesService.list();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedStatuses, result);
        verify(statusRepositoryPort).findAllOrdered();
    }

    @Test
    @DisplayName("Should return empty list when no statuses exist")
    void shouldReturnEmptyListWhenNoStatusesExist() {
        when(statusRepositoryPort.findAllOrdered()).thenReturn(Collections.emptyList());

        List<Status> result = statusesService.list();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statusRepositoryPort).findAllOrdered();
    }

    @Test
    @DisplayName("Should create status with correct position")
    void shouldCreateStatusSuccessfully() {
        String name = "New Status";
        int maxPosition = 2;

        when(statusRepositoryPort.getMaxPosition()).thenReturn(maxPosition);

        Status result = statusesService.create(name);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(name, result.name());
        assertEquals(maxPosition + 1, result.position());
        assertNotNull(result.createdAt());
        verify(statusRepositoryPort).getMaxPosition();
        verify(statusRepositoryPort).save(any(Status.class));
    }

    @Test
    @DisplayName("Should create first status at position 1")
    void shouldCreateFirstStatusAtPositionOne() {
        String name = "First Status";

        when(statusRepositoryPort.getMaxPosition()).thenReturn(0);

        Status result = statusesService.create(name);

        assertNotNull(result);
        assertEquals(1, result.position());
        verify(statusRepositoryPort).getMaxPosition();
        verify(statusRepositoryPort).save(any(Status.class));
    }

    @Test
    @DisplayName("Should rename status successfully")
    void shouldRenameStatusSuccessfully() {
        String statusId = UUID.randomUUID().toString();
        String newName = "Updated Status";
        Instant createdAt = Instant.now();

        Status existingStatus = new Status(UUID.fromString(statusId), "Old Status", 1, createdAt);
        when(statusRepositoryPort.getById(statusId)).thenReturn(existingStatus);

        statusesService.rename(statusId, newName);

        verify(statusRepositoryPort).getById(statusId);
        verify(statusRepositoryPort).save(argThat(status -> status.id().equals(existingStatus.id()) &&
                status.name().equals(newName) &&
                status.position() == existingStatus.position() &&
                status.createdAt().equals(existingStatus.createdAt())));
    }

    @Test
    @DisplayName("Should delete status when no tasks are assigned")
    void shouldDeleteStatusWhenNoTasksAssigned() {
        String statusId = UUID.randomUUID().toString();

        when(taskRepositoryPort.countByStatusId(statusId)).thenReturn(0L);

        statusesService.delete(statusId);

        verify(taskRepositoryPort).countByStatusId(statusId);
        verify(statusRepositoryPort).delete(statusId);
    }

    @Test
    @DisplayName("Should throw exception when deleting status with assigned tasks")
    void shouldThrowExceptionWhenDeletingStatusWithTasks() {
        String statusId = UUID.randomUUID().toString();

        when(taskRepositoryPort.countByStatusId(statusId)).thenReturn(5L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> statusesService.delete(statusId));

        assertTrue(exception.getReason().contains("Cannot delete status with tasks assigned"));
        verify(taskRepositoryPort).countByStatusId(statusId);
        verify(statusRepositoryPort, never()).delete(statusId);
    }

    @Test
    @DisplayName("Should update status position")
    void shouldUpdateStatusPosition() {
        String statusId = UUID.randomUUID().toString();
        int newPosition = 5;
        Instant createdAt = Instant.now();

        Status existingStatus = new Status(UUID.fromString(statusId), "Status", 1, createdAt);
        when(statusRepositoryPort.getById(statusId)).thenReturn(existingStatus);

        statusesService.updatePosition(statusId, newPosition);

        verify(statusRepositoryPort).getById(statusId);
        verify(statusRepositoryPort).save(argThat(status -> status.id().equals(existingStatus.id()) &&
                status.name().equals(existingStatus.name()) &&
                status.position() == newPosition &&
                status.createdAt().equals(existingStatus.createdAt())));
    }

    @Test
    @DisplayName("Should swap positions between two statuses")
    void shouldSwapTwoStatusesSuccessfully() {
        String aId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        Instant now = Instant.now();

        Status statusA = new Status(UUID.fromString(aId), "Status A", 1, now);
        Status statusB = new Status(UUID.fromString(bId), "Status B", 2, now);

        when(statusRepositoryPort.getById(aId)).thenReturn(statusA);
        when(statusRepositoryPort.getById(bId)).thenReturn(statusB);

        statusesService.swap(aId, bId);

        verify(statusRepositoryPort).getById(aId);
        verify(statusRepositoryPort).getById(bId);
        verify(statusRepositoryPort, times(2)).save(any(Status.class));

        verify(statusRepositoryPort).save(argThat(status -> status.id().equals(statusA.id()) &&
                status.name().equals(statusA.name()) &&
                status.position() == statusB.position()));

        verify(statusRepositoryPort).save(argThat(status -> status.id().equals(statusB.id()) &&
                status.name().equals(statusB.name()) &&
                status.position() == statusA.position()));
    }
}
