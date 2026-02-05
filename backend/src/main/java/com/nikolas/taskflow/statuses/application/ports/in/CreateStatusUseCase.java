package com.nikolas.taskflow.statuses.application.ports.in;

import com.nikolas.taskflow.statuses.domain.Status;

public interface CreateStatusUseCase {
    Status create(String name);
}
