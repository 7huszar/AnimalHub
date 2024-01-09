import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Mode} from "../../../enums/modes";

@Component({
  selector: 'app-favorites-card',
  templateUrl: './favorites-card.component.html',
  styleUrls: ['./favorites-card.component.scss']
})
export class FavoritesCardComponent {

  @Input() name :string;
  @Input() id :number | unknown;
  @Output() remove = new EventEmitter<void>();


  protected readonly Mode = Mode;
}
