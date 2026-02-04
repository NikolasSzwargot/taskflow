package com.nikolas.taskflow.tasks.application.ports.in;

import org.springframework.stereotype.Service;

import java.util.UUID;

public interface ChangeTaskStatusUseCase {
    void change(UUID taskId, UUID statusId, int position);
}
