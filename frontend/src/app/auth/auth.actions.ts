import { createAction, props } from '@ngrx/store';

export const setupUserData = createAction(
  '[Auth] Setup user data',
  props<{ username: string | undefined, isLoggedIn: boolean, roles: string[] }>()
);

export const logout = createAction(
  '[Auth] Logout'
);
