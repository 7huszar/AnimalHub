import {Component, Input} from '@angular/core';
import {AnimalDto} from "../../dtos/animal";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {AnimalService} from "../../services/animal.service";
import {AnimalDetailDto} from "../../dtos/animal";
import {Type} from "../../enums/type";
import {Mode} from "../../enums/modes";

@Component({
  selector: 'app-animal-list',
  templateUrl: './animal-list.component.html',
  styleUrls: ['./animal-list.component.scss']
})
export class AnimalListComponent {

  @Input() animals: AnimalDto[] = [];
  displayedAnimals: AnimalDto[] = [];
  p: number;
  searchTermSpecies: String;
  searchTermName: String;
  animalImageURL: any = null;

  constructor(private router: Router,
              public authService: AuthService,
              private animalService: AnimalService) {
  }

  ngOnInit(): void {
      this.displayAnimalCards('all');
  }

  displayAnimalCards(filter: string): void {
    this.displayedAnimals = this.animals
      .filter(animal => filter === 'all' || animal.species === filter)
  }

  editAnimal(animalId: String): void {
    if (this.authService.isAdmin() || this.authService.isMember()) {
      this.router.navigate(['/animal/edit', animalId]).then(r => console.log(r));
    } else {
      this.router.navigate(['/animal', animalId]).then(r => console.log(r));
    }
  }
  onInput(): void {
    if (this.searchTermName) {
      this.displayedAnimals = this.animals
        .filter(animal => animal.name.toLowerCase().includes(this.searchTermName.toLowerCase()))
    }
    if (this.searchTermSpecies) {
      this.displayedAnimals = this.animals
        .filter(animal => animal.species.toLowerCase().includes(this.searchTermSpecies.toLowerCase()))
    }


  protected loadAnimalPic(animalId: string): any {
    this.animalService.getProfileImage(animalId).subscribe({
      next: data => {
        console.log("loaded image ", data);
        this.animalImageURL = URL.createObjectURL(data);
        return this.animalImageURL;
      }, error: err => {
        console.error(err)
      }
    });
  }


  protected readonly Type = Type;
  protected readonly Mode = Mode;
}
