import { Component } from '@angular/core';
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-create-project-modal',
  templateUrl: './create-project-modal.component.html',
  styleUrl: './create-project-modal.component.css',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})
export class CreateProjectModalComponent {

}
