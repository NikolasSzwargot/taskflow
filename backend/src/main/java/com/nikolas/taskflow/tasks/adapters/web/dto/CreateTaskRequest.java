package com.nikolas.taskflow.tasks.adapters.web.dto;

import java.util.UUID;

public record CreateTaskRequest(
        String title,
        String description,
        UUID statusId
) {}
