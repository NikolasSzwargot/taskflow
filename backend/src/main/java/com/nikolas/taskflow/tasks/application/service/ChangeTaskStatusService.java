package com.nikolas.taskflow.tasks.application.service;

import com.nikolas.taskflow.tasks.application.ports.in.ChangeTaskStatusUseCase;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeTaskStatusService implements ChangeTaskStatusUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    public ChangeTaskStatusService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Transactional
    @Override
    public void change(UUID taskId, UUID statusId, int position) {
        taskRepositoryPort.updateStatusAndPosition(taskId, statusId, position);
    }
}
