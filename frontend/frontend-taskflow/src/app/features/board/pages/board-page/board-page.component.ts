import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../../../core/auth/auth.service';
import { StatusesApi } from '../../data-access/statuses.api';
import { TasksApi } from '../../data-access/tasks.api';
import { StatusDto, TaskDto } from '../../models/board.models';

@Component({
  selector: 'app-board-page',
  standalone: true,
  imports: [
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './board-page.component.html',
})
export class BoardPageComponent implements OnInit {
  loading = true;
  statuses: StatusDto[] = [];
  tasks: TaskDto[] = [];

  constructor(
    private auth: AuthService,
    private router: Router,
    private statusesApi: StatusesApi,
    private tasksApi: TasksApi,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loading = true;

    forkJoin({
      statuses: this.statusesApi.getStatuses(),
      tasks: this.tasksApi.getTasks(),
    })
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: ({ statuses, tasks }) => {
          this.statuses = statuses.sort((a, b) => a.position - b.position);
          this.tasks = tasks;
        },
        error: (err) => {
          console.error('Error loading board data', err);
        },
      });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
