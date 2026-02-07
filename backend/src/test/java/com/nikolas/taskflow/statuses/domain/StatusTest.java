package com.nikolas.taskflow.statuses.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Status Domain Model Tests")
class StatusTest {

    @Test
    @DisplayName("Should create status with all fields correctly initialized")
    void shouldCreateStatusWithAllFields() {

        UUID id = UUID.randomUUID();
        String name = "To Do";
        int position = 1;
        Instant createdAt = Instant.now();

        Status status = new Status(id, name, position, createdAt);

        assertNotNull(status);
        assertEquals(id, status.id());
        assertEquals(name, status.name());
        assertEquals(position, status.position());
        assertEquals(createdAt, status.createdAt());
    }

    @Test
    @DisplayName("Should support equality for statuses with same values")
    void shouldSupportEqualityForSameValues() {

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Status status1 = new Status(id, "To Do", 1, now);
        Status status2 = new Status(id, "To Do", 1, now);

        assertEquals(status1, status2);
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when statuses have different IDs")
    void shouldNotBeEqualForDifferentIds() {

        Instant now = Instant.now();

        Status status1 = new Status(UUID.randomUUID(), "To Do", 1, now);
        Status status2 = new Status(UUID.randomUUID(), "To Do", 1, now);

        assertNotEquals(status1, status2);
    }
}
