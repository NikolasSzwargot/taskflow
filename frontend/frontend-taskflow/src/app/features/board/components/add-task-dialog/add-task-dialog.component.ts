import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';

type AddTaskForm = FormGroup<{
  title: FormControl<string>;
  description: FormControl<string>;
}>;

@Component({
  standalone: true,
  imports: [MatDialogModule, ReactiveFormsModule, MatButtonModule, MatInputModule],
  template: `
    <h2 mat-dialog-title>Add task</h2>

    <form [formGroup]="form" (ngSubmit)="submit()">
      <mat-dialog-content>
        <mat-form-field style="width:100%">
          <mat-label>Title</mat-label>
          <input matInput formControlName="title" />
        </mat-form-field>

        <mat-form-field style="width:100%">
          <mat-label>Description</mat-label>
          <textarea matInput rows="3" formControlName="description"></textarea>
        </mat-form-field>
      </mat-dialog-content>

      <mat-dialog-actions align="end">
        <button mat-button type="button" (click)="close()">Cancel</button>
        <button mat-flat-button color="primary" type="submit" [disabled]="form.invalid">
          Create
        </button>
      </mat-dialog-actions>
    </form>
  `,
})
export class AddTaskDialogComponent {
  form: AddTaskForm;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddTaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { statusId: string }
  ) {
    this.form = this.fb.nonNullable.group({
      title: ['', Validators.required],
      description: [''],
    }) as AddTaskForm;
  }

  submit() {
    this.dialogRef.close(this.form.getRawValue());
  }

  close() {
    this.dialogRef.close();
  }
}
