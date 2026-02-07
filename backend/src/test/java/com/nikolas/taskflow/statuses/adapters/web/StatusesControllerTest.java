package com.nikolas.taskflow.statuses.adapters.web;

import com.nikolas.taskflow.statuses.application.ports.in.*;
import com.nikolas.taskflow.statuses.domain.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatusesController.class)
@WithMockUser
@DisplayName("Statuses Controller API Tests")
class StatusesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListStatusesUseCase listStatusesUseCase;

    @MockBean
    private CreateStatusUseCase createStatusUseCase;

    @MockBean
    private RenameStatusUseCase renameStatusUseCase;

    @MockBean
    private DeleteStatusUseCase deleteStatusUseCase;

    @MockBean
    private UpdateStatusPositionUseCase updateStatusPositionUseCase;

    @MockBean
    private SwapStatusesUseCase swapStatusesUseCase;

    @Test
    @DisplayName("GET /api/statuses should return all statuses")
    void shouldListAllStatuses() throws Exception {

        Instant now = Instant.now();
        Status status1 = new Status(UUID.randomUUID(), "To Do", 1, now);
        Status status2 = new Status(UUID.randomUUID(), "In Progress", 2, now);
        Status status3 = new Status(UUID.randomUUID(), "Done", 3, now);

        List<Status> statuses = Arrays.asList(status1, status2, status3);
        when(listStatusesUseCase.list()).thenReturn(statuses);

        mockMvc.perform(get("/api/statuses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("To Do"))
                .andExpect(jsonPath("$[1].name").value("In Progress"))
                .andExpect(jsonPath("$[2].name").value("Done"));

        verify(listStatusesUseCase).list();
    }

    @Test
    @DisplayName("GET /api/statuses should return empty list when no statuses exist")
    void shouldReturnEmptyListWhenNoStatuses() throws Exception {

        when(listStatusesUseCase.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/statuses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(listStatusesUseCase).list();
    }

    @Test
    @DisplayName("POST /api/statuses should create a new status")
    void shouldCreateStatus() throws Exception {

        Instant now = Instant.now();
        UUID statusId = UUID.randomUUID();
        Status createdStatus = new Status(statusId, "New Status", 1, now);

        when(createStatusUseCase.create(eq("New Status"))).thenReturn(createdStatus);

        String requestBody = "{\"name\":\"New Status\"}";

        mockMvc.perform(post("/api/statuses")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(statusId.toString()))
                .andExpect(jsonPath("$.name").value("New Status"))
                .andExpect(jsonPath("$.position").value(1));

        verify(createStatusUseCase).create("New Status");
    }

    @Test
    @DisplayName("PATCH /api/statuses/{id} should rename status")
    void shouldRenameStatus() throws Exception {

        String statusId = UUID.randomUUID().toString();
        String requestBody = "{\"name\":\"Updated Name\"}";

        mockMvc.perform(patch("/api/statuses/{id}", statusId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(renameStatusUseCase).rename(statusId, "Updated Name");
    }

    @Test
    @DisplayName("PATCH /api/statuses/{id}/position should update position")
    void shouldUpdateStatusPosition() throws Exception {

        String statusId = UUID.randomUUID().toString();
        String requestBody = "{\"position\":5}";

        mockMvc.perform(patch("/api/statuses/{id}/position", statusId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(updateStatusPositionUseCase).updatePosition(statusId, 5);
    }

    @Test
    @DisplayName("DELETE /api/statuses/{id} should delete status")
    void shouldDeleteStatus() throws Exception {

        String statusId = UUID.randomUUID().toString();

        mockMvc.perform(delete("/api/statuses/{id}", statusId)
                .with(csrf()))
                .andExpect(status().isOk());

        verify(deleteStatusUseCase).delete(statusId);
    }

    @Test
    @DisplayName("POST /api/statuses/swap should swap two statuses")
    void shouldSwapStatuses() throws Exception {

        String aId = UUID.randomUUID().toString();
        String bId = UUID.randomUUID().toString();
        String requestBody = String.format("{\"aId\":\"%s\",\"bId\":\"%s\"}", aId, bId);

        mockMvc.perform(post("/api/statuses/swap")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(swapStatusesUseCase).swap(aId, bId);
    }
}
