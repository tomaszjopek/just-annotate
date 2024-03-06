import { Component, inject, OnInit } from '@angular/core';
import { Store } from "@ngrx/store";
import { Project, ProjectState, selectAllProjects } from "../../projects.reducer";
import { AsyncPipe } from "@angular/common";
import { loadProjects } from "../../projects.actions";
import { Observable } from "rxjs";
import { MatCard, MatCardTitle } from "@angular/material/card";

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [
    AsyncPipe,
    MatCard,
    MatCardTitle
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

}
