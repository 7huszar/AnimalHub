import { Component } from '@angular/core';
import {AnimalService} from "../../services/animal.service";
import {Animal} from "../../dtos/animal";

@Component({
  selector: 'app-animal-list',
  templateUrl: './animal-list.component.html',
  styleUrls: ['./animal-list.component.scss']
})
export class AnimalListComponent {

  private animals: Animal[] =  [];
  displayedAnimals: any[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 9;

  constructor(private animalService: AnimalService) {
  }

  ngOnInit(): void {
    this.animalService.findAll().subscribe(animals => {
      this.animals = animals;
      this.displayAnimalCards('all');
    });
  }

  displayAnimalCards(filter: string): void {
    const startItem = (this.currentPage - 1) * this.itemsPerPage;
    const endItem = this.currentPage * this.itemsPerPage;
    this.displayedAnimals = this.animals
      .filter(animal => filter === 'all' || animal.species === filter)
      .slice(startItem, endItem);
  }

  nextPage(): void {
    if (this.currentPage * this.itemsPerPage < this.animals.length) {
      this.currentPage++;
      this.displayAnimalCards('all');
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.displayAnimalCards('all');
    }
  }

  editAnimal(animalId: number): void {
    // Implement logic to edit the animal with the specified ID
    console.log('Editing animal with ID:', animalId);
  }

}
