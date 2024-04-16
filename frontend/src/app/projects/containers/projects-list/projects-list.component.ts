import { Component, inject } from '@angular/core';
import { Store } from "@ngrx/store";
import { Project, selectAllProjects } from "../../projects.reducer";
import { AsyncPipe } from "@angular/common";
import { createProject, loadProjects } from "../../projects.actions";
import { Observable } from "rxjs";
import { MatListModule } from "@angular/material/list";
import { selectUsernameIsAdmin } from "../../../auth/auth.reducer";
import { MatDialog } from "@angular/material/dialog";
import { CreateProjectModalComponent } from "../../components/create-project-modal/create-project-modal.component";
import { MatIcon } from "@angular/material/icon";
import { MatPaginator } from "@angular/material/paginator";
import { MatButton } from "@angular/material/button";
import { MatRipple } from "@angular/material/core";

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [
    AsyncPipe,
    MatListModule,
    MatIcon,
    MatPaginator,
    MatButton,
    MatRipple
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
    const dialogRef = this.dialog.open(CreateProjectModalComponent, {
      width: '600px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.store.dispatch(createProject(result))
    });
  }
}
