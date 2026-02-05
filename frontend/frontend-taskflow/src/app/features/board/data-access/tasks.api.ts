import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/enviroment.development';
import { TaskDto } from '../models/board.models';

@Injectable({ providedIn: 'root' })
export class TasksApi {
    constructor(private http: HttpClient) { }

    getTasks() {
        return this.http.get<TaskDto[]>(`${environment.apiBaseUrl}/api/tasks`);
    }

    updateTaskStatus(taskId: string, statusId: string) {
        return this.http.patch(`${environment.apiBaseUrl}/api/tasks/${taskId}/status`, {
            statusId,
        });
    }
}
