import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { from, switchMap } from "rxjs";
import { logout } from "./auth.actions";
import { KeycloakService } from "keycloak-angular";

export const login = createEffect(
  (actions$ = inject(Actions), keycloakService = inject(KeycloakService)) => {
    return actions$.pipe(
      ofType(logout),
      switchMap(() => {
        return from(keycloakService.logout())
      })
    )
  },
  {functional: true, dispatch: false}
)
