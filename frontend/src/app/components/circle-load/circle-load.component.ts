import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-circle-load',
  templateUrl: './circle-load.component.html',
  styleUrls: ['./circle-load.component.scss']
})
export class CircleLoadComponent {
  @Input() radius: number;
  @Input() time: number;
  @Input() start: boolean = false;

  fill: number = 0.0;

  protected readonly Math = Math;
}
