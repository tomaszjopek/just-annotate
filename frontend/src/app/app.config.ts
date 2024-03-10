import { APP_INITIALIZER, ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideState, provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';

import { authFeature } from "./auth/auth.reducer";
import * as authFeatures from "./auth/auth.effects";
import { KeycloakAngularModule, KeycloakBearerInterceptor, KeycloakService } from "keycloak-angular";
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi } from "@angular/common/http";
import { provideStoreDevtools } from "@ngrx/store-devtools";
import { environment } from "../environments/environment";

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
        checkLoginIframe: false,
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html'
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      loadUserProfileAtStartUp: true
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
    provideEffects(authFeatures),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: environment.production,
    }),
    provideState(authFeature),
    importProvidersFrom(KeycloakAngularModule),
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
