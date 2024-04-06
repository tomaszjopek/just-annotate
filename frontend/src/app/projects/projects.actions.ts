import { createAction, props } from '@ngrx/store';
import { Project } from "./projects.reducer";

export enum ProjectType {
  TEXT
}

export interface Label {
  name: string;
  color: string;
}

export interface CreateProjectAction {
  name: string;
  description?: string;
  projectType: ProjectType;
  labels: Label[]
}

export interface ProjectResponse {
   id: string,
   name: string,
   description?: string,
   projectType: string,
   owner: string,
   createdAt: Date,
   labels: Label[]
}


export const loadProjects = createAction(
  '[Projects] Load projects'
);

export const loadProjectsSuccess = createAction(
  '[Projects] Load projects success',
  props<{ projects: Project[] }>()
);

export const createProject = createAction(
  '[Projects] Create project',
  props<CreateProjectAction>()
)

export const createProjectSuccess = createAction(
  '[Projects] Create project success',
  props<ProjectResponse>()
)

export const createProjectError = createAction(
  '[Projects] Create project error'
)
