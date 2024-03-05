import { Routes } from '@angular/router';
import { inject } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { DashboardComponent } from "./dashboard/dashboard/dashboard.component";
import { ProjectsListComponent } from "./projects/projects-list/projects-list.component";
import { from } from "rxjs";

export const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [() => {
      const keycloakService = inject(KeycloakService)

      if (!keycloakService.isLoggedIn()) {
        from(keycloakService.login({
          redirectUri: window.location.origin
        })).subscribe()
      }

    }]
  },
  {
    path: 'projects',
    component: ProjectsListComponent
  }
];
