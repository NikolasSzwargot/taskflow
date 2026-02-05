package com.nikolas.taskflow.tasks.application.service;

import com.nikolas.taskflow.tasks.adapters.persistence.JpaTaskRepositoryAdapter;
import com.nikolas.taskflow.tasks.application.ports.in.ChangeTaskStatusUseCase;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeTaskStatusService implements ChangeTaskStatusUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter;

    public ChangeTaskStatusService(TaskRepositoryPort taskRepositoryPort, JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.jpaTaskRepositoryAdapter = jpaTaskRepositoryAdapter;
    }

    @Transactional
    @Override
    public void change(UUID taskId, UUID statusId ) {
        int position = jpaTaskRepositoryAdapter.findMaxPositionByStatusId(statusId) + 1;
        taskRepositoryPort.updateStatusAndPosition(taskId, statusId, position);
    }
}
