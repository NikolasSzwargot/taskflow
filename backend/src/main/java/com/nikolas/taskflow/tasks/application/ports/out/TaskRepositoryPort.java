package com.nikolas.taskflow.tasks.application.ports.out;

import com.nikolas.taskflow.tasks.domain.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepositoryPort {
    List<Task> findAllOrdered();
    Task save (Task task);
    Optional<Task> findById(UUID id);
    void updateStatusAndPosition(UUID taskId, UUID statusId, int position);
}
