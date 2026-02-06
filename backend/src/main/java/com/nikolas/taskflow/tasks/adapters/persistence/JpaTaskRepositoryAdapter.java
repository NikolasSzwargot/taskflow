package com.nikolas.taskflow.tasks.adapters.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.nikolas.taskflow.tasks.adapters.persistence.jpa.entity.TaskEntity;
import com.nikolas.taskflow.tasks.adapters.persistence.jpa.repository.TaskJpaRepository;
import com.nikolas.taskflow.tasks.application.ports.out.TaskRepositoryPort;
import com.nikolas.taskflow.tasks.domain.Task;

@Repository
public class JpaTaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskJpaRepository;

    public JpaTaskRepositoryAdapter(TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }

    @Override
    public List<Task> findAllOrdered() {
        return taskJpaRepository.findAllByOrderByStatusIdAscPositionAsc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Task save(Task task) {
        var entity = new TaskEntity(
                task.id(),
                task.title(),
                task.description(),
                task.statusId(),
                task.position(),
                task.createdAt(),
                task.updatedAt()
        );
        return toDomain(taskJpaRepository.save(entity));
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void updateStatusAndPosition(UUID taskId, UUID statusId, int position) {
        taskJpaRepository.updateStatusAndPosition(taskId, statusId, position);
    }

    @Override
    public int findMaxPositionByStatusId(UUID statusId) {
        return taskJpaRepository.findMaxPositionByStatusId(statusId);
    }

    @Override
    public long countByStatusId(String statusId) {
        return taskJpaRepository.countByStatusId(UUID.fromString(statusId));
    }

    private Task toDomain(TaskEntity e) {
        return new Task(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getStatusId(),
                e.getPosition(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
