import {Component, Input, OnInit} from '@angular/core';
import {AnimalListDto} from "../../../dtos/animal";

@Component({
  selector: 'app-animal-card',
  templateUrl: './animal-card.component.html',
  styleUrls: ['./animal-card.component.scss']
})
export class AnimalCardComponent implements OnInit{

  @Input() animal :AnimalListDto;

  like() :void {
    console.log("like");
  }

  dislike() :void {
    console.log("dislike")
  }

  ngOnInit(): void {
  }
}
