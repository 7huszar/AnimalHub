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
  @Input() shelterId: string;
  displayedAnimals: AnimalDto[] = [];
  p: number;
  searchTermSpecies: String;
  searchTermName: String;

  constructor(private router: Router,
              public authService: AuthService,
              private animalService: AnimalService) {
  }

  typeForDeletion: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';


  animalForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
    this.typeForDeletion = "Shelter";
  }
  ngOnInit(): void {
    this.displayAnimalCards('all');
  }

  displayAnimalCards(filter: string): void {
    this.displayedAnimals = this.animals
      .filter(animal => filter === 'all' || animal.species === filter)
  }

  editAnimal(animalId: String): void {
    if (this.isPrivileged()) {
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

  isPrivileged(): boolean {
    return this.authService.isAdmin() || this.authService.isMemberOf(this.shelterId);
  }

  protected readonly Mode = Mode;


  addAnimal() {
    this.router.navigate(['/animal/create', this.shelterId]).then(r => console.log(r));
  }
}
