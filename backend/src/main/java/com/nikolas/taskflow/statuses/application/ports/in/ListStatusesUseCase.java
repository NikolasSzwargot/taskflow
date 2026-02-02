package com.nikolas.taskflow.statuses.application.ports.in;

import com.nikolas.taskflow.statuses.domain.Status;

import java.util.List;

public interface ListStatusesUseCase {
    List<Status> list();
}
