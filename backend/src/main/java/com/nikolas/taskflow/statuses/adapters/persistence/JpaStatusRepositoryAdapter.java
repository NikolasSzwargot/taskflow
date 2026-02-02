package com.nikolas.taskflow.statuses.adapters.persistence;

import com.nikolas.taskflow.statuses.adapters.persistence.jpa.repository.StatusJpaRepository;
import com.nikolas.taskflow.statuses.application.ports.out.StatusRepositoryPort;
import com.nikolas.taskflow.statuses.domain.Status;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaStatusRepositoryAdapter implements StatusRepositoryPort {

    private final StatusJpaRepository statusJpaRepository;

    public JpaStatusRepositoryAdapter(StatusJpaRepository statusJpaRepository) {
        this.statusJpaRepository = statusJpaRepository;
    }

    @Override
    public List<Status> findAllOrdered() {
        return statusJpaRepository.findAllByOrderByPositionAsc()
                .stream()
                .map(status -> new Status(status.getId(), status.getName(), status.getPosition(), status.getCreatedAt()))
                .toList();
    }
}