import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-circle-load',
  templateUrl: './circle-load.component.html',
  styleUrls: ['./circle-load.component.scss']
})
export class CircleLoadComponent {
  @Input() radius: number;
  @Input() time: number;

  protected readonly Math = Math;
  fill: number = 0.0;

  click() :void{
    this.fill = 1.0;
  }
}
