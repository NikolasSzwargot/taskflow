package com.nikolas.taskflow.statuses.application.ports.out;

import com.nikolas.taskflow.statuses.domain.Status;

import java.util.List;

public interface StatusRepositoryPort {
    List<Status> findAllOrdered();
}
