import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { StatusDto } from '../../../../board/models/board.models';
import { StatusesApi } from '../../../../board/data-access/statuses.api';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-admin-statuses-page',
    standalone: true,
    imports: [CommonModule, MatCardModule, MatButtonModule, MatInputModule, MatIconModule, FormsModule],
    templateUrl: './admin-statuses-page.component.html',
    styleUrl: './admin-statuses-page.component.scss',
})
export class AdminStatusesPageComponent implements OnInit {
    loading = true;
    statuses: StatusDto[] = [];

    newName = '';

    constructor(
        private api: StatusesApi,
        private toastr: ToastrService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit() {
        this.reload();
    }

    reload() {
        this.loading = true;
        this.api.getStatuses()
            .pipe(finalize(() => {
                this.loading = false;
                this.cdr.detectChanges();
            }))
            .subscribe({
                next: (s) => {
                    this.statuses = s.slice().sort((a, b) => a.position - b.position);
                    this.cdr.detectChanges();
                },
                error: () => this.toastr.error('Failed to load statuses'),
            });
    }

    add() {
        const name = this.newName.trim();
        if (!name) return;

        this.api.createStatus({ name }).subscribe({
            next: () => {
                this.newName = '';
                this.toastr.success('Status created');
                this.reload();
            },
            error: () => this.toastr.error('Failed to create status'),
        });
    }

    rename(status: StatusDto, name: string) {
        const trimmed = name.trim();
        if (!trimmed || trimmed === status.name) return;

        this.api.renameStatus(status.id, trimmed).subscribe({
            next: () => {
                this.toastr.success('Status updated');
                this.reload();
            },
            error: () => this.toastr.error('Failed to update status'),
        });
    }

    remove(status: StatusDto) {
        this.api.deleteStatus(status.id).subscribe({
            next: () => {
                this.toastr.success('Status deleted');
                this.reload();
            },
            error: (error: HttpErrorResponse) => {
                if (error.status === 409) {
                    this.toastr.error('Cannot delete status with tasks assigned');
                } else {
                    this.toastr.error('Failed to delete status');
                }
            },
        });
    }

    moveUp(i: number) {
        if (i <= 0) return;
        const a = this.statuses[i - 1];
        const b = this.statuses[i];
        this.swapPositions(a, b);
    }

    moveDown(i: number) {
        if (i >= this.statuses.length - 1) return;
        const a = this.statuses[i];
        const b = this.statuses[i + 1];
        this.swapPositions(a, b);
    }

    private swapPositions(a: StatusDto, b: StatusDto) {
        const prev = this.statuses.slice();

        const aPos = a.position;
        a.position = b.position;
        b.position = aPos;

        this.statuses = this.statuses.slice().sort((x, y) => x.position - y.position);

        this.api.swap(a.id, b.id).subscribe({
            next: () => {
                this.toastr.success('Reordered');
            },
            error: () => {
                this.statuses = prev;
                this.toastr.error('Failed to reorder');
            }
        });
    }
}
