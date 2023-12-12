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

  private animalList :AnimalDetailDto[];

  constructor(
    public authService: AuthService,
    private animalService :AnimalService,
    private notifier :NotifierService,
    private translator :TranslateService) { }

  ngOnInit() {
    this.animalService.findAll().subscribe({
      next :data => {
        this.animalList = data;
      },
      error :error => {
        console.error("Failed to load animals", error);
      }
    });
  }

  loadCard(position :Position) {
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

  protected readonly AuthService = AuthService;
  protected readonly Position = Position;
}

enum Position {
  LEFT,
  MIDDLE,
  RIGHT
}
