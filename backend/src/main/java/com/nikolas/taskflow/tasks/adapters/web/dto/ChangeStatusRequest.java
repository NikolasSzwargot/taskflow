package com.nikolas.taskflow.tasks.adapters.web.dto;

import java.util.UUID;

public record ChangeStatusRequest(
        UUID statusId,
        int position
) {}
