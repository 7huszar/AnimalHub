import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnimalHomepageComponent } from './animal-homepage.component';

describe('AnimalHomepageComponent', () => {
  let component: AnimalHomepageComponent;
  let fixture: ComponentFixture<AnimalHomepageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnimalHomepageComponent]
    });
    fixture = TestBed.createComponent(AnimalHomepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
