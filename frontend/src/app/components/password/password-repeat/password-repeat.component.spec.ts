import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordRepeatComponent } from './password-repeat.component';

describe('PasswordRepeatComponent', () => {
  let component: PasswordRepeatComponent;
  let fixture: ComponentFixture<PasswordRepeatComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordRepeatComponent]
    });
    fixture = TestBed.createComponent(PasswordRepeatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
