import { Routes } from '@angular/router';
import { DashboardComponent } from "./dashboard/dashboard/dashboard.component";
import { ProjectsListComponent } from "./projects/containers/projects-list/projects-list.component";
import { authenticatedGuard } from "./core/guards/authenticated.guard";
import { provideState } from "@ngrx/store";
import { projectsFeature } from "./projects/projects.reducer";
import { provideEffects } from "@ngrx/effects";
import * as projectsEffects from './projects/projects.effects'


export const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authenticatedGuard]
  },
  {
    path: 'projects',
    component: ProjectsListComponent,
    canActivate: [authenticatedGuard],
    providers: [
      provideState(projectsFeature),
      provideEffects(projectsEffects)
    ]
  }
];
