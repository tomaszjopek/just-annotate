import { Component, inject, OnInit } from '@angular/core';
import { Store } from "@ngrx/store";
import { Project, ProjectState, selectAllProjects } from "../../projects.reducer";
import { AsyncPipe } from "@angular/common";
import { loadProjects } from "../../projects.actions";
import { Observable } from "rxjs";

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [
    AsyncPipe
  ],
  templateUrl: './projects-list.component.html',
  styleUrl: './projects-list.component.scss'
})
export class ProjectsListComponent implements OnInit {

  readonly store: Store<ProjectState> = inject(Store<ProjectState>)

  projects$: Observable<Project[]> = this.store.select(selectAllProjects)

  constructor() {
    this.store.dispatch(loadProjects())
  }

  ngOnInit(): void {
    // this.loadProjects();
  }

  // private loadProjects() {
  //   this.store.dispatch(loadProjects())
  // }
}
