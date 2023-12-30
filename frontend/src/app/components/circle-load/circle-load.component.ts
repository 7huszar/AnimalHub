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
  finished: boolean = false;
  remain: number = 0;
  protected readonly Math = Math;

  startCountdown() :void {
    this.remain = this.time - 1;
    this.color = 'red';
    const intv = setInterval(() => {
      if (this.remain <= 0) {
        this.finished = true;
        this.color = 'green';
        clearInterval(intv);
      }
      if (this.remain > 0) {
        this.remain--;
      }
    }, 1000);
    this.start = true;
  }
}
