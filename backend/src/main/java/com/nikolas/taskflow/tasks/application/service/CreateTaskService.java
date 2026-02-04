package com.nikolas.taskflow.tasks.application.service;

import com.nikolas.taskflow.tasks.application.ports.in.CreateTaskUseCase;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import com.nikolas.taskflow.tasks.domain.Task;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    public CreateTaskService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Task create(String title, String description, UUID statusId) {
        Instant now = Instant.now();
        int nextPosition = taskRepositoryPort.findMaxPositionByStatusId(statusId) + 1;
        Task task = new Task(UUID.randomUUID(), title, description, statusId, nextPosition, now, now);
        return  taskRepositoryPort.save(task);
    }
}
