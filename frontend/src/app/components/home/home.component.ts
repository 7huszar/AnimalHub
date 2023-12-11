import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Animal} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {NotifierService} from "../../services/notifier.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private left :Animal;
  private middle :Animal;
  private right :Animal;

  private animalList :Animal[];
  private defaultAnimal: Animal = {
    name: "Schnappi das kleine Krokodil",
    id: "1",
    species: null,
    race: null,
    birthDate: null
  };

  constructor(
    public authService: AuthService,
    private animalService :AnimalService,
    private notifier :NotifierService,
    private translator :TranslateService) { }

  ngOnInit() {
    this.setAllCards();
  }

  /**
   * replaces one of the animals shown in the cards.
   *
   * @param position
   */
  setCard(position: Position) {
    if (this.animalList && this.animalList.length > 0) {
      switch (position) {
        case Position.LEFT:
          this.left = this.animalList.pop();
          break;
        case Position.MIDDLE:
          this.middle = this.animalList.pop();
          break;
        case Position.RIGHT:
          this.right = this.animalList.pop();
      }
    }

    return this.defaultAnimal;
  }

  loadCards() :void {
    this.animalService.findAll().subscribe({
      next :data => {
        this.animalList = data;
        console.log(this.animalList);
      },
      error :error => {
        console.error("Failed to load animals", error);
        let title :string = 'error';
        this.translator.get("HOME.LOAD_FAILED").subscribe( (translation :string) => {
          title = translation;
        });
        this.notifier.notifyError(error, title);
      }
    });
  }

    /**
   * used to get the animals according to position, to use
   *
   * @param position
   */
  getCard(position: Position): Animal {
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
    return this.defaultAnimal;
  }

  setAllCards() {
    if (this.animalList) {
      this.setCard(Position.LEFT);
      this.setCard(Position.MIDDLE);
      this.setCard(Position.RIGHT);
    } else {
      this.loadCards();
      this.setAllCardsOnce()
    }
  }

  private setAllCardsOnce() {
    if (this.animalList) {
      this.setCard(Position.LEFT);
      this.setCard(Position.MIDDLE);
      this.setCard(Position.RIGHT);
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
