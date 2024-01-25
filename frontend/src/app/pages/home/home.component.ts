import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {AnimalDetailDto} from "../../dtos/animal";
import {NotifierService} from "../../services/notifier.service";
import {ToastrService} from "ngx-toastr";
import {PreferencesService} from "../../services/preferences.service";
import {removeElement} from "../../util/util";
import {map, Observable} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  protected animalList: AnimalDetailDto[] = [];
  protected likedAnimals: AnimalDetailDto[] = [];

  private pos = 0;

  constructor(
    public authService: AuthService,
    private notification: ToastrService,
    private notifier: NotifierService,
    private preferencesService: PreferencesService) {
  }

  ngOnInit() {
    this.getNextAnimals(3).subscribe();

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

  getAnimal(position: Position): AnimalDetailDto {
    if (this.animalList.length > this.pos + position) {
      return this.animalList[this.pos + position];
    } else {
      return null;
    }
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
      next: () => {
        this.likedAnimals.push(animal);
        this.getNextAnimals(1).subscribe();
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
      next: () => {
        this.getNextAnimals(1).subscribe();
      },
      error: err => {
        // TODO notify
      }
    });
  }

  // TODO handle error
  private getNextAnimals(numAnimals: number): Observable<any> {
    return this.preferencesService.getRecommendedAnimals(this.authService.getUser().id).pipe(
      map(animals => {
        let addedAnimals = 0;
        for (let animal of animals) {
          if (addedAnimals >= numAnimals) {
            break;
          }
          if (!this.animalList.find(element => element.id === animal.id)) {
            this.animalList.push(animal);
            addedAnimals++;
          }
        }
      })
    );
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
    if (this.pos + Position.RIGHT + 3 <= this.animalList.length - 1) {
      this.pos += 3;
    } else if (this.pos + Position.RIGHT + 3 > this.animalList.length - 1) {
      const amount = this.pos + Position.RIGHT + 3 - (this.animalList.length - 1);

      this.getNextAnimals(amount).subscribe(() => {
        this.pos = this.animalList.length - (Position.RIGHT + 1);
      });
    }
  }

  leftClick(): void {
    if (this.pos > 3) {
      this.pos -= 3;
    } else {
      this.pos = 0;
    }
  }

  protected readonly Position = Position;
}

enum Position {
  LEFT,
  MIDDLE,
  RIGHT
}
