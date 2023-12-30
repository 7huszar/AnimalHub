import {Component, Input, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'app-circle-load',
  templateUrl: './circle-load.component.html',
  styleUrls: ['./circle-load.component.scss'],
  animations: [
      trigger('load', [
        state('hateMyself', style({
          'stroke-dashoffset': '{{perimeter}}',
        }), {params: {perimeter: 60}}),
          state('start', style({
            'stroke-dashoffset': '{{perimeter}}',
          }), {params: {perimeter: 60}}),
          state('end', style({
            'stroke-dashoffset': '0',
          })),
          transition('start => end', [
              animate('{{time}}s')
          ], {params: {time: 60}}),
          transition("end => start", [
              animate('0s')
          ]),
          transition('hateMyself => start', [
            animate('0s')
          ])
      ])
  ]
})
export class CircleLoadComponent implements OnInit{
  @Input() radius: number;
  @Input() time: number;

  perimeter: number = 170;
  fill: number = 0.0;
  color: string = 'green';
  start: boolean = false;
  finished: boolean = false;
  remain: number = 0;
  state: string = 'hateMyself';
  protected readonly Math = Math;

  startCountdown() :void {
    this.finished = false;
    this.state = 'start';
    this.remain = this.time - 1;
    this.color = 'red';
    const intv = setInterval(() => {
      if (this.remain <= 0) {
        this.color = 'green';
        this.start = false;
        this.finished = true;
        clearInterval(intv);
      }
      if (this.remain > 0) {
        this.remain--;
      }
    }, 1000);
    this.start = true;
  }

  inStart(event: AnimationEvent) {
    if (this.state === 'start') {
      this.state = 'end';
    }
  }

  ngOnInit() {
    this.perimeter = (this.radius - 16) * 2 * Math.PI;
  }
}
