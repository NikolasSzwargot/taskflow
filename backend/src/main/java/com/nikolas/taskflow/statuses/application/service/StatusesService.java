package com.nikolas.taskflow.statuses.application.service;

import com.nikolas.taskflow.statuses.application.ports.in.*;
import com.nikolas.taskflow.statuses.application.ports.out.StatusRepositoryPort;
import com.nikolas.taskflow.statuses.domain.Status;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class StatusesService implements
        ListStatusesUseCase,
        CreateStatusUseCase,
        RenameStatusUseCase,
        DeleteStatusUseCase,
        UpdateStatusPositionUseCase,
        SwapStatusesUseCase{

    private final StatusRepositoryPort statusRepositoryPort;
    private final TaskRepositoryPort taskRepositoryPort;

    public StatusesService(StatusRepositoryPort statusRepositoryPort, TaskRepositoryPort taskRepositoryPort) {
        this.statusRepositoryPort = statusRepositoryPort;
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public List<Status> list() {
        return statusRepositoryPort.findAllOrdered();
    }

    @Override
    public Status create(String name) {
        int position = statusRepositoryPort.getMaxPosition() + 1;
        Status status = new Status(UUID.randomUUID(), name, position, Instant.now());
        statusRepositoryPort.save(status);
        return status;
    }

    @Override
    public void rename(String id, String name) {
        Status status = statusRepositoryPort.getById(id);
        Status renamedStatus = new Status(
                status.id(),
                name,
                status.position(),
                status.createdAt()
        );

        statusRepositoryPort.save(renamedStatus);
    }

    @Override
    public void delete(String id) {
        long count = taskRepositoryPort.countByStatusId(id);
        if (count > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete status with tasks assigned"
            );
        }
        statusRepositoryPort.delete(id);
    }

    @Override
    public void updatePosition(String id, int position) {
        Status status = statusRepositoryPort.getById(id);
        Status updatedPositionStatus = new Status(
                status.id(),
                status.name(),
                position,
                status.createdAt()
        );
        statusRepositoryPort.save(updatedPositionStatus);
    }

    @Override
    @Transactional
    public void swap(String aId, String bId) {
        Status a = statusRepositoryPort.getById(aId);
        Status b = statusRepositoryPort.getById(bId);

        statusRepositoryPort.save(new Status(a.id(), a.name(), b.position(), a.createdAt()));
        statusRepositoryPort.save(new Status(b.id(), b.name(), a.position(), b.createdAt()));
    }

}
