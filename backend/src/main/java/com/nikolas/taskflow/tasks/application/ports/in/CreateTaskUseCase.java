package com.nikolas.taskflow.tasks.application.ports.in;

import com.nikolas.taskflow.tasks.domain.Task;

import java.util.UUID;

public interface CreateTaskUseCase {
    Task create(String title, String description, UUID statusId);
}
