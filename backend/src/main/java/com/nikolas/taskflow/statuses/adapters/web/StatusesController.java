package com.nikolas.taskflow.statuses.adapters.web;

import com.nikolas.taskflow.statuses.application.ports.in.ListStatusesUseCase;
import com.nikolas.taskflow.statuses.domain.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Statuses")
@RestController
public class StatusesController {

    private final ListStatusesUseCase listStatusesUseCase;

    public StatusesController(ListStatusesUseCase listStatusesUseCase) {
        this.listStatusesUseCase = listStatusesUseCase;
    }

    @GetMapping("/api/statuses")
    public List<Status> list() {
        return listStatusesUseCase.list();
    }
}
