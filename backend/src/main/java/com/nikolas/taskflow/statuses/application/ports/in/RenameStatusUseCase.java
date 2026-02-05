package com.nikolas.taskflow.statuses.application.ports.in;

public interface RenameStatusUseCase {
    void rename(String statusId, String name);
}
