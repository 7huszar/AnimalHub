import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AnimalDetailDto} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {NotifierService} from "../../services/notifier.service";
import {TranslateService} from "@ngx-translate/core";

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
  protected likedAnimals: AnimalDetailDto[];
  protected dislikedAnimals: AnimalDetailDto[];
  counter = 1;


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
    if (this.animalList && this.animalList.length > 0 && this.counter < this.animalList.length) {
      this.right = this.animalList[this.counter - 1];
      this.middle = this.animalList[this.counter];
      this.left = this.animalList[this.counter + 1];
    }
  }

  getCard(position: Position): AnimalDetailDto {
    if (this.animalList && this.animalList.length > 0) {
      switch (position) {
        case Position.LEFT:
          return this.left;
        case Position.MIDDLE:
          return this.middle
        case Position.RIGHT:
          return this.right;
        default:
          console.error("unknown card position", position);
          return null;
      }
    }
    return null;
  }

  like(animal: AnimalDetailDto) {
    this.likedAnimals.push(animal);
    console.log(animal);
  }
  dislike(animal: AnimalDetailDto) {
    this.dislikedAnimals.push(animal);
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
}

enum Position {
  LEFT,
  MIDDLE,
  RIGHT
}
