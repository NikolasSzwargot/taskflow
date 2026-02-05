package com.nikolas.taskflow.statuses.adapters.persistence;

import com.nikolas.taskflow.statuses.adapters.persistence.jpa.entity.StatusEntity;
import com.nikolas.taskflow.statuses.adapters.persistence.jpa.repository.StatusJpaRepository;
import com.nikolas.taskflow.statuses.application.ports.out.StatusRepositoryPort;
import com.nikolas.taskflow.statuses.domain.Status;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Status getById(String id) {
        return statusJpaRepository.findById(UUID.fromString(id))
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Status not found: " + id));
    }

    @Override
    public void save(Status status) {
        statusJpaRepository.save(toEntity(status));
    }

    @Override
    public void delete(String id) {
        statusJpaRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public int getMaxPosition() {
        return statusJpaRepository
                .findTopByOrderByPositionDesc()
                .map(StatusEntity::getPosition)
                .orElse(0);
    }

    private Status toDomain(StatusEntity status) {
        return new Status(
                status.getId(),
                status.getName(),
                status.getPosition(),
                status.getCreatedAt()
        );
    }

    private StatusEntity toEntity(Status status) {
        StatusEntity statusEntity = new StatusEntity(
                status.id(),
                status.name(),
                status.position(),
                status.createdAt());
        return statusEntity;
    }
}
