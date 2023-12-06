import { Component } from '@angular/core';
import {Animal} from "../../dtos/animal";

@Component({
  selector: 'app-animal-homepage',
  templateUrl: './animal-homepage.component.html',
  styleUrls: ['./animal-homepage.component.scss']
})
export class AnimalHomepageComponent {

  animal: Animal = {
    birthDate: undefined,
    createdOn: undefined,
    height: 0,
    id: "",
    name: "",
    race: "",
    shelter: undefined,
    species: undefined,
    weight: 0
  }

}
