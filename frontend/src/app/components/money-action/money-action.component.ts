import {Component, Input} from '@angular/core';
import {ShelterDetailDto} from "../../dtos/shelter";
import {AnimalDetailDto} from "../../dtos/animal";

@Component({
  selector: 'app-money-action',
  templateUrl: './money-action.component.html',
  styleUrls: ['./money-action.component.scss']
})
export class MoneyActionComponent {
  @Input() shelter: ShelterDetailDto;
  @Input() animal: AnimalDetailDto;
}
