import { createEntityAdapter, EntityAdapter, EntityState } from "@ngrx/entity";
import { createFeature, createReducer, on } from "@ngrx/store";
import { loadProjectsSuccess } from "./projects.actions";

export interface Project {
  id: string;
  name: string;
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

const {
  selectIds,
  selectAll
} = projectAdapter.getSelectors()

export const selectAllProjects = selectAll
