import { Component, inject, OnInit } from '@angular/core';
import { Store } from "@ngrx/store";
import { Project, ProjectState, selectAllProjects } from "../../projects.reducer";
import { AsyncPipe } from "@angular/common";
import { loadProjects } from "../../projects.actions";
import { Observable } from "rxjs";
import { MatCard, MatCardActions, MatCardContent, MatCardTitle } from "@angular/material/card";
import { MatButton } from "@angular/material/button";
import { MatList, MatListItem, MatListItemTitle } from "@angular/material/list";

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
    MatList,
    MatListItem,
    MatListItemTitle
  ],
  templateUrl: './projects-list.component.html',
  styleUrl: './projects-list.component.scss'
})
export class ProjectsListComponent {

  readonly store: Store<ProjectState> = inject(Store<ProjectState>)

  projects$: Observable<Project[]> = this.store.select(selectAllProjects)

  constructor() {
    this.store.dispatch(loadProjects())
  }

  editProject(project: Project): void {
    // TODO edit project
  }

  removeProject(project: Project): void {
    // TODO remove project
  }
}
