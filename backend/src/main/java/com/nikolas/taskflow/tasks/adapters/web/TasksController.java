package com.nikolas.taskflow.tasks.adapters.web;

import com.nikolas.taskflow.tasks.adapters.web.dto.ChangeStatusRequest;
import com.nikolas.taskflow.tasks.adapters.web.dto.CreateTaskRequest;
import com.nikolas.taskflow.tasks.application.ports.in.ChangeTaskStatusUseCase;
import com.nikolas.taskflow.tasks.application.ports.in.CreateTaskUseCase;
import com.nikolas.taskflow.tasks.application.ports.in.ListTasksUseCase;
import com.nikolas.taskflow.tasks.domain.Task;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tasks")
@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    private final ListTasksUseCase listTasksUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final ChangeTaskStatusUseCase changeTaskStatusUseCase;

    public TasksController(ListTasksUseCase listTasksUseCase, CreateTaskUseCase createTaskUseCase, ChangeTaskStatusUseCase changeTaskStatusUseCase) {
        this.listTasksUseCase = listTasksUseCase;
        this.createTaskUseCase = createTaskUseCase;
        this.changeTaskStatusUseCase = changeTaskStatusUseCase;
    }

    @GetMapping
    public List<Task> list() {
        return listTasksUseCase.list();
    }

    @PostMapping
    public Task create(@RequestBody CreateTaskRequest req) {
        return createTaskUseCase.create(req.title(), req.description(), req.statusId());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable("id") UUID id, @RequestBody ChangeStatusRequest req) {
        changeTaskStatusUseCase.change(id, req.statusId(), req.position());
        return ResponseEntity.noContent().build();
    }
}
