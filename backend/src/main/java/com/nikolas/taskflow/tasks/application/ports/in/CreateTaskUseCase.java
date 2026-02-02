package com.nikolas.taskflow.tasks.application.ports.in;

import com.nikolas.taskflow.tasks.domain.Task;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CreateTaskUseCase {
    Task create(String title, String description, UUID statusId, int position);
}
