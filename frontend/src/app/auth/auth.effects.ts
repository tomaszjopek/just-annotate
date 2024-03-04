import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { loginAction, loginSuccess } from "./auth.actions";
import { of, switchMap } from "rxjs";

export const login = createEffect(
  (actions$ = inject(Actions)) => {
    return actions$.pipe(
      ofType(loginAction),
      switchMap(action => {
        return of(loginSuccess({token: 'received tokendasdsada'}))
      })
    )
  },
  {functional: true}
)
