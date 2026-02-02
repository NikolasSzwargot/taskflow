package com.nikolas.taskflow.statuses.application.service;

import com.nikolas.taskflow.statuses.application.ports.in.ListStatusesUseCase;
import com.nikolas.taskflow.statuses.application.ports.out.StatusRepositoryPort;
import com.nikolas.taskflow.statuses.domain.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListStatusesService implements ListStatusesUseCase {

    private final StatusRepositoryPort statusRepositoryPort;

    public ListStatusesService(StatusRepositoryPort statusRepositoryPort) {
        this.statusRepositoryPort = statusRepositoryPort;
    }

    @Override
    public List<Status> list() {
        return statusRepositoryPort.findAllOrdered();
    }
}
