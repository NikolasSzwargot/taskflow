package com.nikolas.taskflow.statuses.adapters.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "statuses")
public class StatusEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int position;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected StatusEntity() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public int getPosition() {
        return position;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
}
