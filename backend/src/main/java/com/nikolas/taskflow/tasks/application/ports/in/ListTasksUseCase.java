package com.nikolas.taskflow.tasks.application.ports.in;

import com.nikolas.taskflow.tasks.domain.Task;

import java.util.List;

public interface ListTasksUseCase {
    List<Task> list();
}
