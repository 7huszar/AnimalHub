import {Component, OnInit} from '@angular/core';
import {Mode} from "../../enums/modes";
import {AnimalListDto, AnimalSearch} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {Globals} from "../../global/globals";
import {NgModel} from "@angular/forms";

@Component({
  selector: 'app-animal-search',
  templateUrl: './animal-search.component.html',
  styleUrls: ['./animal-search.component.scss']
})
export class AnimalSearchComponent implements OnInit {

  protected readonly Mode = Mode;

  animalSearch: AnimalSearch = {
    name: undefined,
    gender: "",
    species: "",
    race: undefined,
    bornBefore: undefined,
    bornAfter: undefined,
    heavierThan: undefined,
    lighterThan: undefined,
    tallerThan: undefined,
    smallerThan: undefined
  };

  birthDateMode: string = "BORN_AFTER";

  heightMode: string = "TALLER_THAN";

  weightMode: string = "HEAVIER_THAN";


  animals: AnimalListDto[] = [];

  protected pageNumber: number;

  private timeoutId: any;

  today: String = new Date().toISOString().slice(0, 10);

  constructor(private animalService: AnimalService, protected globals: Globals) {
  }

  ngOnInit(): void {
    this.onInput();
  }

  onTextInput(): void {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }

    this.timeoutId = setTimeout(() => {
      this.onInput();
    }, 300);
  }

  onInput(): void {
    this.animalService.search(this.animalSearch).subscribe({
      next: returnAnimals => {
        this.animals = returnAnimals;
      },
      error: error => {
        console.error("Failed to load animals", error);
      }
    });
  }

  dateModifierSelectChange() {
    if (this.birthDateMode === "BORN_AFTER") {
      this.animalSearch.bornBefore = undefined;
    } else {
      this.animalSearch.bornAfter = undefined;
    }
    this.onInput();
  }

  heightModifierSelectChange() {
    if (this.heightMode === "TALLER_THAN") {
      this.animalSearch.smallerThan = undefined;
    } else {
      this.animalSearch.tallerThan = undefined;
    }
    this.onInput();
  }

  weightModifierSelectChange() {
    if (this.weightMode === "HEAVIER_THAN") {
      this.animalSearch.lighterThan = undefined;
    } else {
      this.animalSearch.heavierThan = undefined;
    }
    this.onInput();
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }
}
