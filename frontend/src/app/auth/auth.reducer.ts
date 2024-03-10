import { createFeature, createReducer, on } from "@ngrx/store";
import { setupUserData } from "./auth.actions";

export interface AuthState {
  username: string | undefined,
  isLoggedIn: boolean | undefined,
  error: string | undefined,
  loading: boolean
}

const initialState: AuthState = {
  username: undefined,
  isLoggedIn: false,
  error: undefined,
  loading: false
}

export const authFeature = createFeature({
  name: 'auth',
  reducer: createReducer(
    initialState,
    on(setupUserData, (authState, {username, isLoggedIn}) => ({
      ...authState,
      username,
      isLoggedIn
    }))
  )
})

export const {
  name,
  reducer,
  selectUsername,
  selectIsLoggedIn,
  selectLoading,
  selectError
} = authFeature
