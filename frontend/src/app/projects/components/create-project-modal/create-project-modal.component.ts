import { Component } from '@angular/core';
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle } from "@angular/material/card";
import { MatError, MatFormField, MatLabel } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { MatIcon } from "@angular/material/icon";
import { JsonPipe } from "@angular/common";

@Component({
  selector: 'app-create-project-modal',
  templateUrl: './create-project-modal.component.html',
  styleUrl: './create-project-modal.component.css',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, ReactiveFormsModule, MatLabel, MatError, MatCardTitle, MatCard, MatCardHeader, MatCardContent, MatFormField, MatInput, MatIcon, MatCardActions, JsonPipe],
})
export class CreateProjectModalComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<CreateProjectModalComponent>) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      type: ['', Validators.required],
      labels: this.fb.array([
        this.initLabel()
      ])
    });
  }

  initLabel() {
    return this.fb.group({
      name: ['', Validators.required],
      color: ['']
    });
  }

  get labels(): FormGroup[] {
    return (this.form.controls['labels'] as FormArray).controls as FormGroup[]
  }

  addNewLabel() {
    const control = this.form.controls['labels'] as FormArray;
    control.push(this.initLabel());
  }

  removeLabel(index: number): void {
    const control = this.form.controls['labels'] as FormArray;
    control.removeAt(index);
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value)
    } else {
      this.form.markAllAsTouched();
    }
  }
}
