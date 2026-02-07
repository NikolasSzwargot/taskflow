package com.nikolas.taskflow.tasks.adapters.web;

import com.nikolas.taskflow.tasks.application.ports.in.ChangeTaskStatusUseCase;
import com.nikolas.taskflow.tasks.application.ports.in.CreateTaskUseCase;
import com.nikolas.taskflow.tasks.application.ports.in.ListTasksUseCase;
import com.nikolas.taskflow.tasks.domain.Task;
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

@WebMvcTest(TasksController.class)
@WithMockUser
@DisplayName("Tasks Controller API Tests")
class TasksControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ListTasksUseCase listTasksUseCase;

        @MockBean
        private CreateTaskUseCase createTaskUseCase;

        @MockBean
        private ChangeTaskStatusUseCase changeTaskStatusUseCase;

        @Test
        @DisplayName("GET /api/tasks should return all tasks")
        void shouldListAllTasks() throws Exception {

                UUID statusId = UUID.randomUUID();
                Instant now = Instant.now();

                Task task1 = new Task(UUID.randomUUID(), "Task 1", "Description 1", statusId, 1, now, now);
                Task task2 = new Task(UUID.randomUUID(), "Task 2", "Description 2", statusId, 2, now, now);

                List<Task> tasks = Arrays.asList(task1, task2);
                when(listTasksUseCase.list()).thenReturn(tasks);

                mockMvc.perform(get("/api/tasks"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Task 1"))
                                .andExpect(jsonPath("$[1].title").value("Task 2"));

                verify(listTasksUseCase).list();
        }

        @Test
        @DisplayName("GET /api/tasks should return empty list when no tasks exist")
        void shouldReturnEmptyListWhenNoTasks() throws Exception {

                when(listTasksUseCase.list()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/api/tasks"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(listTasksUseCase).list();
        }

        @Test
        @DisplayName("POST /api/tasks should create a new task")
        void shouldCreateTask() throws Exception {

                UUID statusId = UUID.randomUUID();
                UUID taskId = UUID.randomUUID();
                Instant now = Instant.now();

                Task createdTask = new Task(taskId, "New Task", "Task Description", statusId, 1, now, now);
                when(createTaskUseCase.create(eq("New Task"), eq("Task Description"), eq(statusId)))
                                .thenReturn(createdTask);

                String requestBody = String.format(
                                "{\"title\":\"New Task\",\"description\":\"Task Description\",\"statusId\":\"%s\"}",
                                statusId);

                mockMvc.perform(post("/api/tasks")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(taskId.toString()))
                                .andExpect(jsonPath("$.title").value("New Task"))
                                .andExpect(jsonPath("$.description").value("Task Description"))
                                .andExpect(jsonPath("$.statusId").value(statusId.toString()));

                verify(createTaskUseCase).create("New Task", "Task Description", statusId);
        }

        @Test
        @DisplayName("PATCH /api/tasks/{id}/status should change task status")
        void shouldChangeTaskStatus() throws Exception {

                UUID taskId = UUID.randomUUID();
                UUID newStatusId = UUID.randomUUID();

                String requestBody = String.format("{\"statusId\":\"%s\"}", newStatusId);

                mockMvc.perform(patch("/api/tasks/{id}/status", taskId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isNoContent());

                verify(changeTaskStatusUseCase).change(taskId, newStatusId);
        }
}
