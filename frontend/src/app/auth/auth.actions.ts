import { createAction, props } from '@ngrx/store';

export const loginAction = createAction(
  '[Login] User Login',
  props<{ username: string, password: string }>()
);

export const loginSuccess = createAction(
  '[Login] Login Success',
  props<{ token: string }>()
);

export const loginFailure = createAction(
  '[Login] Login Failure',
  props<{ error: string }>()
);
