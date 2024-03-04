import { Component } from '@angular/core';
import { Store } from "@ngrx/store";
import { selectUsername } from "../auth.reducer";
import { Observable } from "rxjs";
import { AsyncPipe } from "@angular/common";

@Component({
  selector: 'app-username',
  standalone: true,
  templateUrl: './username.component.html',
  imports: [
    AsyncPipe
  ],
  styleUrl: './username.component.scss'
})
export class UsernameComponent {

  username$: Observable<string | undefined> = this.store.select(selectUsername)

  constructor(private store: Store) {
    this.username$.subscribe((username) => console.log('Testing', username))
  }

}
