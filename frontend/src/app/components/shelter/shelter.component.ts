import {Component, OnInit} from '@angular/core';


@Component({
  selector: 'app-shelter',
  templateUrl: './shelter.component.html',
  styleUrls: ['./shelter.component.scss']
})
export class ShelterComponent implements OnInit {
  private animals = [
    { id: 1, name: 'Lion', type: 'mammal' },
    { id: 2, name: 'Eagle', type: 'bird' },
    // Add more animals as needed
  ];

  displayedAnimals: any[] = [];

  ngOnInit(): void {
    this.displayAnimalCards('all');
  }

  displayAnimalCards(filter: string): void {
    this.displayedAnimals = this.animals.filter(animal => filter === 'all' || animal.type === filter);
  }

  editAnimal(animalId: number): void {
    // Implement logic to edit the animal with the specified ID
    console.log('Editing animal with ID:', animalId);
  }

  onFilterChange(selectedFilter: string): void {
    this.displayAnimalCards(selectedFilter);
  }
}
