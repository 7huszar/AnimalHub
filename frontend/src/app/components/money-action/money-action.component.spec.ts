import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoneyActionComponent } from './money-action.component';

describe('MoneyActionComponent', () => {
  let component: MoneyActionComponent;
  let fixture: ComponentFixture<MoneyActionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MoneyActionComponent]
    });
    fixture = TestBed.createComponent(MoneyActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
