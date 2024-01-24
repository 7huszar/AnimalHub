import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AnimalDetailDto} from "../../dtos/animal";
import {NotifierService} from "../../services/notifier.service";
import {ToastrService} from "ngx-toastr";
import {PreferencesService} from "../../services/preferences.service";
import {removeElement} from "../../util/util";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private left: AnimalDetailDto;
  private middle: AnimalDetailDto;
  private right: AnimalDetailDto;

  protected animalList: AnimalDetailDto[] = [];
  protected likedAnimals: AnimalDetailDto[] = [];

  counter = 1;
  emptyLeft: boolean = false;
  emptyMiddle: boolean = false;
  emptyRight: boolean = false;

  constructor(
    public authService: AuthService,
    private notification: ToastrService,
    private notifier: NotifierService,
    private preferencesService: PreferencesService) {
  }

  ngOnInit() {
    this.initAnimals();

    const user = this.authService.getUser();

    this.preferencesService.getLikedDislikedAnimals(user.id, true).subscribe( {
      next: data => {
        this.likedAnimals = data;
      },
      error: error => {
        console.error("Failed to load liked animals", error);
      }
    })
  }

  loadCards() {
    if (this.animalList && this.animalList.length >= 0) {
      if (this.animalList[this.counter + 1] == undefined && this.animalList[this.counter - 2] != undefined) {
        this.counter--;
      }
      this.left = this.animalList[this.counter - 1];
      this.middle = this.animalList[this.counter];
      this.right = this.animalList[this.counter + 1];
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
    // Remove animal
    this.animalList = removeElement(this.animalList, animal);

    const pref = {
      userId: this.authService.getUser().id,
      animalId: animal.id,
      like: true
    }

    this.preferencesService.likeDislike(pref).subscribe({
      next: data => {
        this.likedAnimals.push(animal);
        this.getNextAnimal();
        this.loadCards();
      },
      error: err => {
        // TODO notify
      }
    });
  }

  dislike(animal: AnimalDetailDto) {
    // Remove animal
    this.animalList = removeElement(this.animalList, animal);

    const pref = {
      userId: this.authService.getUser().id,
      animalId: animal.id,
      like: false
    }

    this.preferencesService.likeDislike(pref).subscribe({
      next: data => {
        this.getNextAnimal();
        this.loadCards();
      },
      error: err => {
        // TODO notify
      }
    });
  }

  private initAnimals() {
    this.preferencesService.getRecommendedAnimals(this.authService.getUser().id).subscribe({
        next: data => {
          this.animalList.push(data.shift());
          this.animalList.push(data.shift());
          this.animalList.push(data.shift());
          this.loadCards();
        },
        error: err => {
          // TODO notify
        }
    });
  }

  private getNextAnimal() {
    this.preferencesService.getRecommendedAnimals(this.authService.getUser().id).subscribe({
        next: animals => {
          console.log(animals)
          console.log("animalList:")
          console.log(this.animalList)
          animals.some(animal => {
            if (!this.animalList.find(element => element.id === animal.id)) {
              this.animalList.push(animal);
              this.loadCards();
              return true;
            }
          });
        },
        error: err => {
          // TODO notify
        }
    })
  }

  removeLikedAnimal(animal: AnimalDetailDto){
    // Remove liked animal
    this.likedAnimals = removeElement(this.likedAnimals, animal);

    this.preferencesService.removeLikedAnimal(this.authService.getUser().id, animal.id).subscribe({
      error: err => {
        // TODO notify
      }
    });
  }

  rightClick(): void {
    this.getNextAnimal();
    this.getNextAnimal();
    this.getNextAnimal();

    for (let i = 0; i < 3; i++) {
      if (this.counter + 2 < this.animalList.length) {
        this.right = this.middle;
        this.middle = this.left;
        this.left = this.animalList[this.counter + 2];
        this.counter++;
      }
    }
  }

  leftClick(): void {
    for (let i = 0; i < 3; i++) {
      if (this.counter - 2 >= 0) {
        this.left = this.middle;
        this.middle = this.right;
        this.right = this.animalList[this.counter - 2];
        this.counter--;
      }
    }
  }

  protected readonly Position = Position;
}

enum Position {
  LEFT,
  MIDDLE,
  RIGHT
}
