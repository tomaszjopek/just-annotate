import { Component, OnInit, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatToolbar } from "@angular/material/toolbar";
import { MatIcon } from "@angular/material/icon";
import { MatSidenav, MatSidenavContainer } from "@angular/material/sidenav";
import { MatNavList } from "@angular/material/list";
import { MatCard, MatCardContent } from "@angular/material/card";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatToolbar, MatIcon, MatSidenavContainer, MatSidenav, MatNavList, MatCard, MatCardContent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  @ViewChild("sidenav")
  sidenav: MatSidenav | undefined;

  constructor() {
  }

  ngOnInit() {
  }

  openSidenav() {
    this.sidenav?.open();
  }
}
