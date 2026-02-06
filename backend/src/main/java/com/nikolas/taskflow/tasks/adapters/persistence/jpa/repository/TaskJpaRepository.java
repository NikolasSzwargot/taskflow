package com.nikolas.taskflow.tasks.adapters.persistence.jpa.repository;

import com.nikolas.taskflow.tasks.adapters.persistence.jpa.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findAllByOrderByStatusIdAscPositionAsc();

    @Modifying
    @Query("""
        update TaskEntity t
        set t.statusId = :statusId, t.position = :position
        where t.id = :taskId
    """)
    int updateStatusAndPosition(@Param("taskId") UUID taskId, @Param("statusId") UUID statusId, @Param("position") int position);

    @Query("select coalesce(max(t.position), 0) from TaskEntity t where t.statusId = :statusId")
    int findMaxPositionByStatusId(@Param("statusId") UUID statusId);

    long countByStatusId(UUID statusId);
}
