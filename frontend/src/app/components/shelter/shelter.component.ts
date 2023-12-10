import {Component, OnInit} from '@angular/core';
import {AnimalService} from "../../services/animal.service";
import {Animal} from "../../dtos/animal";
import {Shelter} from "../../dtos/shelter";


@Component({
  selector: 'app-shelter',
  templateUrl: './shelter.component.html',
  styleUrls: ['./shelter.component.scss']
})
export class ShelterComponent implements OnInit {
  private animals: Animal[] = [];

  displayedAnimals: any[] = [];

  constructor(private animalService: AnimalService) {
  }

  ngOnInit(): void {
    this.animalService.findAll().subscribe(animals => {
      this.animals = animals;
      this.displayAnimalCards('all');
    });
    this.displayAnimalCards('all');
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
