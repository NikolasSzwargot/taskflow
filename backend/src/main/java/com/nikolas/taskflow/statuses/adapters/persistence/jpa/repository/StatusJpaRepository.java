package com.nikolas.taskflow.statuses.adapters.persistence.jpa.repository;

import com.nikolas.taskflow.statuses.adapters.persistence.jpa.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusJpaRepository extends JpaRepository<StatusEntity, UUID> {
    Optional<StatusEntity> findTopByOrderByPositionDesc();
    List<StatusEntity> findAllByOrderByPositionAsc();
}
