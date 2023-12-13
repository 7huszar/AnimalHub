import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-animal-card',
  templateUrl: './animal-card.component.html',
  styleUrls: ['./animal-card.component.scss']
})
export class AnimalCardComponent implements OnInit{

  @Input() name :string;
  @Input() id :number | unknown;
  @Output() dislike = new EventEmitter<void>();
  @Output() like = new EventEmitter<void>();


  /*like() :void {
    console.log("like");
  }

  dislike() :void {
    console.log("dislike")
  }

   */

  ngOnInit(): void {
  }
}
