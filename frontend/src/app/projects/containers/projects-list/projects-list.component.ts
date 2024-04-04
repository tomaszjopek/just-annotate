import { Component, inject } from '@angular/core';
import { Store } from "@ngrx/store";
import { Project, selectAllProjects } from "../../projects.reducer";
import { AsyncPipe } from "@angular/common";
import { loadProjects } from "../../projects.actions";
import { Observable } from "rxjs";
import { MatCard, MatCardActions, MatCardContent, MatCardTitle } from "@angular/material/card";
import { MatButton, MatButtonModule } from "@angular/material/button";
import { MatList, MatListItem, MatListItemTitle } from "@angular/material/list";
import { MatIcon } from "@angular/material/icon";
import { MatPaginator } from "@angular/material/paginator";
import { selectUsernameIsAdmin } from "../../../auth/auth.reducer";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { CreateProjectModalComponent } from "../../components/create-project-modal/create-project-modal.component";

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [
    AsyncPipe,
    MatCard,
    MatCardTitle,
    MatCardContent,
    MatCardActions,
    MatButton,
    MatIcon,
    MatList,
    MatListItem,
    MatListItemTitle,
    MatPaginator,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './projects-list.component.html',
  styleUrl: './projects-list.component.scss'
})
export class ProjectsListComponent {

  readonly store: Store = inject(Store)

  projects$: Observable<Project[]> = this.store.select(selectAllProjects)

  userData$: Observable<{ isAdmin: boolean, username: string | undefined }> = this.store.select(selectUsernameIsAdmin)

  constructor(public dialog: MatDialog) {
    this.store.dispatch(loadProjects())
  }

  editProject(project: Project): void {
    // TODO edit project
  }

  removeProject(project: Project): void {
    // TODO remove project
  }

  createProject() {
    const dialogRef = this.dialog.open(CreateProjectModalComponent, {maxHeight: '90vh'});

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
