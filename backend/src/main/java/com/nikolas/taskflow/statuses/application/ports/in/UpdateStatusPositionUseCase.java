package com.nikolas.taskflow.statuses.application.ports.in;

public interface UpdateStatusPositionUseCase {
    void updatePosition(String statusId, int position);
}
