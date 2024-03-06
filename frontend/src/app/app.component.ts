import { Component, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkWithHref, RouterModule, RouterOutlet } from '@angular/router';
import { MatToolbar } from "@angular/material/toolbar";
import { MatIcon } from "@angular/material/icon";
import { MatSidenav, MatSidenavContainer } from "@angular/material/sidenav";
import { MatListItem, MatNavList } from "@angular/material/list";
import { MatCard, MatCardContent } from "@angular/material/card";
import { UsernameComponent } from "./auth/username/username.component";
import { KeycloakAngularModule } from "keycloak-angular";
import { DashboardComponent } from "./dashboard/dashboard/dashboard.component";
import { HttpClientModule } from "@angular/common/http";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLinkWithHref,
    KeycloakAngularModule,
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
    RouterModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  @ViewChild("sidenav")
  sidenav: MatSidenav | undefined;

  isOpen: boolean = false

  toggleSidenav(): void {
    this.isOpen = !this.isOpen
    this.sidenav?.toggle(this.isOpen)
  }
}
