import { Component, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkWithHref, RouterModule, RouterOutlet } from '@angular/router';
import { MatToolbar } from "@angular/material/toolbar";
import { MatIcon } from "@angular/material/icon";
import { MatSidenav, MatSidenavContainer } from "@angular/material/sidenav";
import { MatListItem, MatNavList } from "@angular/material/list";
import { MatCard, MatCardContent } from "@angular/material/card";
import { UsernameComponent } from "./auth/username/username.component";
import { KeycloakService } from "keycloak-angular";
import { DashboardComponent } from "./dashboard/dashboard/dashboard.component";
import { Store } from "@ngrx/store";
import { from, take } from "rxjs";
import { setupUserData } from "./auth/auth.actions";
import { MatButton } from "@angular/material/button";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLinkWithHref,
    MatToolbar,
    MatIcon,
    MatSidenavContainer,
    MatSidenav,
    MatNavList,
    MatCard,
    MatCardContent,
    MatListItem,
    UsernameComponent,
    DashboardComponent,
    RouterLink,
    RouterModule,
    MatButton
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  @ViewChild("sidenav")
  sidenav: MatSidenav | undefined;

  isOpen: boolean = false;

  constructor(private store: Store, private keycloakService: KeycloakService) {
    from(this.keycloakService.loadUserProfile()).pipe(take(1)).subscribe((profile) => {
      this.store.dispatch(setupUserData({username: profile.username, isLoggedIn: this.keycloakService.isLoggedIn()}))
    })
  }

  toggleSidenav(): void {
    this.isOpen = !this.isOpen
    this.sidenav?.toggle(this.isOpen)
  }
}
