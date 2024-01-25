import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Mode} from "../../../enums/modes";
import {AnimalDetailDto} from "../../../dtos/animal";

@Component({
  selector: 'app-animal-card',
  templateUrl: './animal-card.component.html',
  styleUrls: ['./animal-card.component.scss']
})
export class AnimalCardComponent implements OnInit{

  @Input() animal: AnimalDetailDto;
  @Output() dislike = new EventEmitter<void>();
  @Output() like = new EventEmitter<void>();

  ngOnInit(): void {
  }

  protected readonly Mode = Mode;
}
