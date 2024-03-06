import { createFeature, createReducer } from "@ngrx/store";

interface State {
  token: string | undefined,
  username: string | undefined,
  error: string | undefined,
  loading: boolean
}

const initialState: State = {
  token: undefined,
  username: undefined,
  error: undefined,
  loading: false
}

export const authFeature = createFeature({
  name: 'auth',
  reducer: createReducer(
    initialState
  )
})

export const {
  name,
  reducer,
  selectToken,
  selectUsername,
  selectLoading,
  selectError
} = authFeature
