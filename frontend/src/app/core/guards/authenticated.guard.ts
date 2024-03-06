import { CanActivateFn } from '@angular/router';
import { inject } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { from, of, switchMap } from "rxjs";

export const authenticatedGuard: CanActivateFn = (route, state) => {
  const keycloakService = inject(KeycloakService)

  if (!keycloakService.isLoggedIn()) {
    return from(keycloakService.login({
      redirectUri: window.location.origin + state.url
    })).pipe(switchMap(() => of(false)))
  } else {
    return true
  }

};
