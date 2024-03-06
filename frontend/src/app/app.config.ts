import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideState, provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';

import * as authEffects from './auth/auth.effects';
import { authFeature } from "./auth/auth.reducer";
import { KeycloakBearerInterceptor, KeycloakService } from "keycloak-angular";
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi } from "@angular/common/http";

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8081/auth',
        realm: 'just-annotate',
        clientId: 'just-annotate-app'
      },
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html'
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer'
    });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptorsFromDi()
    ),
    provideStore(),
    provideState(authFeature),
    provideEffects(authEffects),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true
    },
    KeycloakService
  ]
};
