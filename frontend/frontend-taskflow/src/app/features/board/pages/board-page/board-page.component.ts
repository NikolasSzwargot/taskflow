import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CdkDragDrop, DragDropModule } from '@angular/cdk/drag-drop';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';


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
    MatProgressSpinnerModule,
    DragDropModule,
    MatSnackBarModule
  ],
  templateUrl: './board-page.component.html',
  styleUrl: './board-page.component.scss',
})
export class BoardPageComponent implements OnInit {
  loading = true;
  statuses: StatusDto[] = [];
  tasks: TaskDto[] = [];
  tasksByStatus: Record<string, TaskDto[]> = {};
  dropListIds: string[] = [];

  constructor(
    private auth: AuthService,
    private router: Router,
    private statusesApi: StatusesApi,
    private tasksApi: TasksApi,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar
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
          this.dropListIds = this.statuses.map((s) => s.id);
          this.tasks = tasks;
          this.tasksByStatus = this.buildTasksByStatus(this.statuses, tasks);

        },
        error: (err) => {
          console.error('Error loading board data', err);
        },
      });
  }

  getTasksForStatus(statusId: string): TaskDto[] {
    return this.tasks
      .filter(t => t.statusId === statusId)
      .slice()
      .sort((a, b) => a.position - b.position);
  }

  private buildTasksByStatus(statuses: StatusDto[], tasks: TaskDto[]) {
    const map: Record<string, TaskDto[]> = {};
    for (const s of statuses) map[s.id] = [];

    for (const t of tasks) {
      (map[t.statusId] ??= []).push(t);
    }

    for (const key of Object.keys(map)) {
      map[key] = map[key].slice().sort((a, b) => a.position - b.position);
    }

    return map;
  }

  drop(event: CdkDragDrop<TaskDto[]>) {
    const prev = structuredClone(this.tasksByStatus);
    const targetStatusId = event.container.id;

    if (event.previousContainer === event.container) {
      // Same column - don't allow reordering, keep at bottom
      return;
    } else {
      // Different column - remove from source and add to end of target
      const movedTask = event.previousContainer.data[event.previousIndex];

      // Remove from previous container
      event.previousContainer.data.splice(event.previousIndex, 1);

      // Add to the END of the target container (bottom of the list)
      event.container.data.push(movedTask);

      // Update the task's status
      movedTask.statusId = targetStatusId;
    }

    const movedTask = event.container.data[event.container.data.length - 1];

    this.tasksApi.updateTaskStatus(movedTask.id, targetStatusId).subscribe({
      next: () => { },
      error: () => {
        this.tasksByStatus = prev;
        this.snackBar.open('Failed to move task', 'Close', { duration: 2500 });
      },
    });
  }


  getList(statusId: string): TaskDto[] {
    return this.tasksByStatus[statusId] ?? [];
  }


  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
