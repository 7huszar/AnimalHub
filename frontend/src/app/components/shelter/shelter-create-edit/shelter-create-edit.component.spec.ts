import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShelterCreateEditComponent } from './shelter-create-edit.component';

describe('ShelterCreateEditComponent', () => {
  let component: ShelterCreateEditComponent;
  let fixture: ComponentFixture<ShelterCreateEditComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShelterCreateEditComponent]
    });
    fixture = TestBed.createComponent(ShelterCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
