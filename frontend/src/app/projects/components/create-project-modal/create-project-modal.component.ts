import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from '@angular/material/dialog';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle } from "@angular/material/card";
import { MatError, MatFormField, MatLabel } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-create-project-modal',
  templateUrl: './create-project-modal.component.html',
  styleUrl: './create-project-modal.component.css',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, ReactiveFormsModule, MatLabel, MatError, MatCardTitle, MatCard, MatCardHeader, MatCardContent, MatFormField, MatInput, MatIcon, MatCardActions],
})
export class CreateProjectModalComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder) {
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

  get labelsControl(): FormArray {
    return this.form.controls['labels'] as FormArray
  }

  addNewLabel() {
    const control = this.form.controls['labels'] as FormArray;
    control.push(this.initLabel());
  }

  removeLabel(index: number) {
    const control = this.form.controls['labels'] as FormArray;
    control.removeAt(index);
  }

  onSubmit() {
    if (this.form.valid) {
      // Process form submission
      console.log(this.form.value);
    } else {
      // Mark fields as touched to display validation errors
      this.form.markAllAsTouched();
    }
  }
}
