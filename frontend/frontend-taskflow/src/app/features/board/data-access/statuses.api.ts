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
}
