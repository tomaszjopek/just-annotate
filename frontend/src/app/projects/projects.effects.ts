import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import {
  createProject,
  createProjectError,
  createProjectSuccess,
  loadProjects,
  loadProjectsSuccess,
  ProjectType
} from "./projects.actions";
import { catchError, map, mergeMap, of, switchMap } from "rxjs";
import { ProjectsService } from "./services/projects.service";

export const loadProjects$ = createEffect(
  (actions$ = inject(Actions)) => {
    return actions$.pipe(
      ofType(loadProjects),
      mergeMap(() => inject(ProjectsService).fetchProjects()),
      map(projects => loadProjectsSuccess({projects}))
    )
  },
  {functional: true}
)

export const createProject$ = createEffect(
  (actions$ = inject(Actions)) => {
      return actions$.pipe(
        ofType(createProject),
        switchMap(payload => {
          const requestBody = {
            name: payload.name,
            description: payload.description,
            type: payload.projectType,
            labels: payload.labels
          }
          return inject(ProjectsService).createProject(requestBody)
        }),
        map(createdProject => createProjectSuccess({
          ...createdProject
        })),
        catchError(error => of(createProjectError()))
      )
  },
  {functional: true}
)
