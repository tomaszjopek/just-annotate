import { createAction, props } from '@ngrx/store';
import { Project } from "./projects.reducer";

export const loadProjects = createAction(
  '[Projects] Load projects'
);

export const loadProjectsSuccess = createAction(
  '[Projects] Load projects success',
  props<{ projects: Project[] }>()
);
