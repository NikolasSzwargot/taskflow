package com.nikolas.taskflow.statuses.application.ports.in;

public interface SwapStatusesUseCase {
    void swap(String aId, String bId);
}
