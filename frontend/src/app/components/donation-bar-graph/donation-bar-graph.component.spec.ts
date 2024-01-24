import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonationBarGraphComponent } from './donation-bar-graph.component';

describe('DonationBarGraphComponent', () => {
  let component: DonationBarGraphComponent;
  let fixture: ComponentFixture<DonationBarGraphComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DonationBarGraphComponent]
    });
    fixture = TestBed.createComponent(DonationBarGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
