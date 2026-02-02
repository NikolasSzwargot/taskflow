package com.nikolas.taskflow.tasks.application.service;

import com.nikolas.taskflow.tasks.application.ports.in.ListTasksUseCase;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import com.nikolas.taskflow.tasks.domain.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTasksService implements ListTasksUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    public ListTasksService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public List<Task> list() {
        return taskRepositoryPort.findAllOrdered();
    }
}
