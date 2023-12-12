import {Component, OnInit} from '@angular/core';
import {AnimalService} from "../../services/animal.service";
import {AnimalDetailDto} from "../../dtos/animal";



@Component({
  selector: 'app-shelter',
  templateUrl: './shelter.component.html',
  styleUrls: ['./shelter.component.scss']
})
export class ShelterComponent implements OnInit {
  private animals: AnimalDetailDto[] = [];
  displayedAnimals: AnimalDetailDto[] = [];

  constructor(private animalService: AnimalService,
  ) {
  }

  // TODO since this only wants the animals of the shelter, shelterservice is needed which will get shelter + its animals
  ngOnInit(): void {
    this.animalService.findAll().subscribe(animals => {
      this.animals = animals;
      this.displayAnimalCards('all');
    });
  }

  displayAnimalCards(filter: string): void {
    this.displayedAnimals = this.animals.filter(animal => filter === 'all' || animal.species === filter);
  }

  editAnimal(animalId: number): void {
    // Implement logic to edit the animal with the specified ID
    console.log('Editing animal with ID:', animalId);
  }

  onFilterChange(selectedFilter: string): void {
    this.displayAnimalCards(selectedFilter);
  }
}
