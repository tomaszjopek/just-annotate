import { createEntityAdapter, EntityAdapter, EntityState } from "@ngrx/entity";
import { createFeature, createFeatureSelector, createReducer, on } from "@ngrx/store";
import { loadProjectsSuccess } from "./projects.actions";

export interface Label {
  name: string;
  color: string;
}

export interface Project {
  id: string;
  name: string;
  description?: string;
  type: string;
  owner: string;
  createdAt: Date;
  labels: Label[]
}

export interface ProjectState extends EntityState<Project> {
}

export const projectAdapter: EntityAdapter<Project> = createEntityAdapter<Project>()

export const initialState: ProjectState = projectAdapter.getInitialState()

export const projectsFeature = createFeature({
  name: 'projects',
  reducer: createReducer(
    initialState,
    on(loadProjectsSuccess, (state, {projects}) => {
      return projectAdapter.addMany(projects, state)
    })
  )
})

export const projectsStateSelector = createFeatureSelector<ProjectState>('projects')

const {
  selectIds,
  selectEntities,
  selectAll
} = projectAdapter.getSelectors(projectsStateSelector)

export const selectProjects = selectEntities

export const selectAllProjects = selectAll
