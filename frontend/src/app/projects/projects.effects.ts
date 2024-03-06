import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { loadProjects, loadProjectsSuccess } from "./projects.actions";
import { map, mergeMap } from "rxjs";
import { ProjectsService } from "./services/projects.service";

export const login = createEffect(
  (actions$ = inject(Actions)) => {
    return actions$.pipe(
      ofType(loadProjects),
      mergeMap(() => inject(ProjectsService).fetchProjects()),
      map(projects => loadProjectsSuccess({projects}))
    )
  },
  {functional: true}
)
