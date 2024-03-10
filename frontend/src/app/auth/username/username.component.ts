import { Component } from '@angular/core';
import { Store } from "@ngrx/store";
import { AuthState, selectUsername } from "../auth.reducer";
import { Observable } from "rxjs";
import { AsyncPipe } from "@angular/common";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-username',
  standalone: true,
  templateUrl: './username.component.html',
  imports: [
    AsyncPipe,
    MatIcon
  ],
  styleUrl: './username.component.scss'
})
export class UsernameComponent {

  username$: Observable<string | undefined> = this.store.select(selectUsername)

  constructor(private store: Store<AuthState>) {
  }

}
