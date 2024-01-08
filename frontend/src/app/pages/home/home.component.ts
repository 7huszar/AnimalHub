import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AnimalDetailDto} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {NotifierService} from "../../services/notifier.service";
import {TranslateService} from "@ngx-translate/core";
import {indexOf} from "lodash";
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private left :AnimalDetailDto;
  private middle :AnimalDetailDto;
  private right :AnimalDetailDto;

  protected animalList: AnimalDetailDto[];
  protected likedAnimals: AnimalDetailDto[] = [];
  protected dislikedAnimals: AnimalDetailDto[] = [];
  counter = 1;
  emptyLeft: boolean = false;
  emptyMiddle: boolean = false;
  emptyRight: boolean = false;

  constructor(
    public authService: AuthService,
    private animalService :AnimalService,
    private notifier :NotifierService,
    private translator :TranslateService) { }

  ngOnInit() {
    this.animalService.findAll().subscribe({
      next :data => {
        this.animalList = data;
        this.loadCards();
        console.log(this.animalList)
      },
      error :error => {
        console.error("Failed to load animals", error);
      }
    });
  }

  loadCards() {
    if (this.animalList && this.animalList.length >= 0) {
      if (this.animalList[this.counter + 1] == undefined && this.animalList[this.counter - 2] != undefined) {
        this.counter--;
      }
      this.right = this.animalList[this.counter - 1];
      this.middle = this.animalList[this.counter];
      this.left = this.animalList[this.counter + 1];
    }
  }

  getCard(position: Position): AnimalDetailDto {
    if (this.animalList && this.animalList.length >= 0) {
      this.emptyLeft = false;
      this.emptyMiddle = false;
      this.emptyRight = false;

      switch (position) {
        case Position.LEFT:
          if (this.left == undefined) this.emptyLeft = true;
          return this.left;
        case Position.MIDDLE:
          if (this.middle == undefined) this.emptyMiddle = true;
          return this.middle
        case Position.RIGHT:
          if (this.right == undefined) this.emptyRight = true;
          return this.right;
        default:
          console.error("unknown card position", position);
          return null;
      }
    }
    return null;
  }

  like(animal: AnimalDetailDto) {
    if (this.dislikedAnimals.includes(animal)) {
      this.dislikedAnimals.splice(this.dislikedAnimals.indexOf(animal), 1);
    }
    if (!this.likedAnimals.includes(animal)) {
      this.likedAnimals.push(animal);
    }
    this.animalList.splice(this.animalList.indexOf(animal), 1);
    this.loadCards();
  }
  dislike(animal: AnimalDetailDto) {
    if (this.likedAnimals.includes(animal)) {
      this.likedAnimals.splice(this.likedAnimals.indexOf(animal), 1);
    }
    if (!this.likedAnimals.includes(animal)) {
      this.dislikedAnimals.push(animal);
    }
    this.animalList.splice(this.animalList.indexOf(animal), 1);
    this.loadCards();
  }
  remove(animal: AnimalDetailDto){
    if (this.likedAnimals.includes(animal)) {
      this.likedAnimals.splice(this.likedAnimals.indexOf(animal), 1);
      this.animalList.push(animal);
      this.loadCards();
    }
  }
  leftClick(): void {
    for (let i = 0; i < 3; i++) {
      if (this.counter + 2 < this.animalList.length) {
        this.right = this.middle;
        this.middle = this.left;
        this.left = this.animalList[this.counter + 2];
        this.counter++;
      }
    }
  }
  rightClick(): void {
    for (let i = 0; i < 3; i++) {
      if (this.counter - 2 >= 0) {
        this.left = this.middle;
        this.middle = this.right;
        this.right = this.animalList[this.counter - 2];
        this.counter--;
      }
    }
  }

  protected readonly AuthService = AuthService;
  protected readonly Position = Position;
  protected readonly name = name;
}

enum Position {
  LEFT,
  MIDDLE,
  RIGHT
}
