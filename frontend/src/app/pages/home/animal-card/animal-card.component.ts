import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Mode} from "../../../enums/modes";

@Component({
  selector: 'app-animal-card',
  templateUrl: './animal-card.component.html',
  styleUrls: ['./animal-card.component.scss']
})
export class AnimalCardComponent implements OnInit{

  @Input() name :string;
  @Input() id :number | unknown;
  @Input() empty: boolean;
  @Output() dislike = new EventEmitter<void>();
  @Output() like = new EventEmitter<void>();

  animalImageURL: any = null;

  ngOnInit(): void {
  }

  protected readonly Mode = Mode;
}
