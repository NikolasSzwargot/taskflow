package com.nikolas.taskflow.statuses.adapters.web;

import com.nikolas.taskflow.statuses.adapters.web.dto.CreateStatusRequest;
import com.nikolas.taskflow.statuses.adapters.web.dto.RenameStatusRequest;
import com.nikolas.taskflow.statuses.adapters.web.dto.SwapStatusesRequest;
import com.nikolas.taskflow.statuses.adapters.web.dto.UpdateStatusPositionRequest;
import com.nikolas.taskflow.statuses.application.ports.in.*;
import com.nikolas.taskflow.statuses.domain.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Statuses")
@RestController
public class StatusesController {

    private final ListStatusesUseCase listStatusesUseCase;
    private final CreateStatusUseCase createStatusUseCase;
    private final RenameStatusUseCase renameStatusUseCase;
    private final DeleteStatusUseCase deleteStatusUseCase;
    private final UpdateStatusPositionUseCase updateStatusPositionUseCase;
    private final SwapStatusesUseCase swapStatusesUseCase;

    public StatusesController(
            ListStatusesUseCase listStatusesUseCase,
            CreateStatusUseCase createStatusUseCase,
            RenameStatusUseCase renameStatusUseCase,
            DeleteStatusUseCase deleteStatusUseCase,
            UpdateStatusPositionUseCase updateStatusPositionUseCase,
            SwapStatusesUseCase swapStatusesUseCase
    ) {
        this.listStatusesUseCase = listStatusesUseCase;
        this.createStatusUseCase = createStatusUseCase;
        this.renameStatusUseCase = renameStatusUseCase;
        this.deleteStatusUseCase = deleteStatusUseCase;
        this.updateStatusPositionUseCase = updateStatusPositionUseCase;
        this.swapStatusesUseCase = swapStatusesUseCase;
    }

    @GetMapping("/api/statuses")
    public List<Status> list() {
        return listStatusesUseCase.list();
    }

    @PostMapping("/api/statuses")
    public Status create(@RequestBody CreateStatusRequest req) {
        return createStatusUseCase.create(req.name());
    }

    @PatchMapping("/api/statuses/{id}")
    public void rename(
            @PathVariable String id,
            @RequestBody RenameStatusRequest req
    ) {
        renameStatusUseCase.rename(id, req.name());
    }

    @PatchMapping("/api/statuses/{id}/position")
    public void updatePosition(
            @PathVariable String id,
            @RequestBody UpdateStatusPositionRequest req
    ) {
        updateStatusPositionUseCase.updatePosition(id, req.position());
    }

    @DeleteMapping("/api/statuses/{id}")
    public void delete(@PathVariable String id) {
        deleteStatusUseCase.delete(id);
    }

    @PostMapping("/api/statuses/swap")
    public void swap(@RequestBody SwapStatusesRequest req) {
        swapStatusesUseCase.swap(req.aId(), req.bId());
    }

}
