import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-circle-load',
  templateUrl: './circle-load.component.html',
  styleUrls: ['./circle-load.component.scss']
})
export class CircleLoadComponent {
  @Input() radius: number;
  @Input() time: number;

  fill: number = 0.0;
  color: string = 'green';
  start: boolean = false;

  protected readonly Math = Math;

  startCountdown() :void {
    this.start = true;
  }
}
