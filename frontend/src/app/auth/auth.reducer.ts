import { createFeature, createReducer, createSelector, on } from "@ngrx/store";
import { setupUserData } from "./auth.actions";

export interface AuthState {
  username: string | undefined,
  roles: string[],
  isLoggedIn: boolean | undefined,
  error: string | undefined,
  loading: boolean
}

const initialState: AuthState = {
  username: undefined,
  roles: [],
  isLoggedIn: false,
  error: undefined,
  loading: false
}

export const authFeature = createFeature({
  name: 'auth',
  reducer: createReducer(
    initialState,
    on(setupUserData, (authState, {username, isLoggedIn, roles}) => ({
      ...authState,
      username,
      roles,
      isLoggedIn
    }))
  )
})

export const {
  name,
  reducer,
  selectUsername,
  selectRoles,
  selectIsLoggedIn,
  selectLoading,
  selectError
} = authFeature

export const selectUsernameIsAdmin = createSelector(
  selectUsername,
  selectRoles,
  (username: string | undefined, roles: string[]) => {
    return {username, isAdmin: roles.includes('admin')}
  }
)
