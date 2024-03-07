import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsernameComponent } from './username.component';
import { MockStore, provideMockStore } from "@ngrx/store/testing";

describe('UsernameComponent', () => {
  let component: UsernameComponent;
  let fixture: ComponentFixture<UsernameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsernameComponent],
      providers: [
        provideMockStore()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsernameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
