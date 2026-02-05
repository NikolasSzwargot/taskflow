package com.nikolas.taskflow.statuses.application.ports.out;

import com.nikolas.taskflow.statuses.domain.Status;

import java.util.List;

public interface StatusRepositoryPort {
    List<Status> findAllOrdered();

    int getMaxPosition();

    void save(Status status);

    Status getById(String id);

    void delete(String id);
}
