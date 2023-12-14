import {Component, Input} from '@angular/core';
import {AnimalDto} from "../../dtos/animal";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {Mode} from "../../enums/modes";
import {AnimalService} from "../../services/animal.service";

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

  }

  protected readonly Mode = Mode;

  deleteAnimal(id: string) {
    this.animalService.delete(id).subscribe({
      next: () => {
        this.animals = this.animals.filter(animal => animal.id !== id);
        this.displayAnimalCards('all');
      },
      error: error => {
        console.error('Error deleting animal', error);
      }
    });
  }
}
