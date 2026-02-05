import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StatusDto } from '../models/board.models';
import { environment } from '../../../../enviroments/enviroment.development';

@Injectable({ providedIn: 'root' })
export class StatusesApi {
    constructor(private http: HttpClient) { }

    getStatuses() {
        return this.http.get<StatusDto[]>(`${environment.apiBaseUrl}/api/statuses`);
    }

    createStatus(input: { name: string }) {
        return this.http.post<StatusDto>(`${environment.apiBaseUrl}/api/statuses`, input);
    }

    renameStatus(id: string, name: string) {
        return this.http.patch<StatusDto>(`${environment.apiBaseUrl}/api/statuses/${id}`, { name });
    }

    deleteStatus(id: string) {
        return this.http.delete<void>(`${environment.apiBaseUrl}/api/statuses/${id}`);
    }

    updatePosition(id: string, position: number) {
        return this.http.patch<StatusDto>(
            `${environment.apiBaseUrl}/api/statuses/${id}/position`,
            { position }
        );
    }

    swap(aId: string, bId: string) {
        return this.http.post<void>(
            `${environment.apiBaseUrl}/api/statuses/swap`,
            { aId, bId }
        );
    }
}
