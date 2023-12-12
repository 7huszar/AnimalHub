import { Component } from '@angular/core';
import {AnimalService} from "../../services/animal.service";
import {AnimalDetailDto} from "../../dtos/animal";

@Component({
  selector: 'app-animal-list',
  templateUrl: './animal-list.component.html',
  styleUrls: ['./animal-list.component.scss']
})
export class AnimalListComponent {

  private animals: AnimalDetailDto[] = [];
  displayedAnimals: any[] = [];
  p: number;

  constructor(private animalService: AnimalService) {
  }

  ngOnInit(): void {
    this.animalService.findAll().subscribe(animals => {
      this.animals = animals;
      this.displayAnimalCards('all');
    });
  }

  displayAnimalCards(filter: string): void {
    this.displayedAnimals = this.animals
      .filter(animal => filter === 'all' || animal.species === filter)
  }

  editAnimal(animalId: number): void {
    // Implement logic to edit the animal with the specified ID
    console.log('Editing animal with ID:', animalId);
  }

}
