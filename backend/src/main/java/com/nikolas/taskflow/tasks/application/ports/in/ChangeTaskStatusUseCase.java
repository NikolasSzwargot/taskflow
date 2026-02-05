package com.nikolas.taskflow.tasks.application.ports.in;

import java.util.UUID;

public interface ChangeTaskStatusUseCase {
    void change(UUID taskId, UUID statusId);
}
