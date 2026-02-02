package com.nikolas.taskflow.tasks.domain;

import java.time.Instant;
import java.util.UUID;

public record Task(
    UUID id,
    String title,
    String description,
    UUID statusId,
    int position,
    Instant createdAt,
    Instant updatedAt
) {}