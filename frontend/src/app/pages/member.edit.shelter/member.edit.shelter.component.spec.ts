import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberEditShelterComponent } from './member.edit.shelter.component';

describe('MemberEditShelterComponent', () => {
  let component: MemberEditShelterComponent;
  let fixture: ComponentFixture<MemberEditShelterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberEditShelterComponent]
    });
    fixture = TestBed.createComponent(MemberEditShelterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
