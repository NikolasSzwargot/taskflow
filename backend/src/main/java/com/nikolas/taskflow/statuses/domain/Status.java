package com.nikolas.taskflow.statuses.domain;

import java.util.UUID;
import java.time.Instant;

public record Status(
    UUID id,
    String name,
    int position,
    Instant createdAt
) {}