import {Component, OnInit} from '@angular/core';
import {AnimalDto} from "../../dtos/animal";
import  {ShelterService} from "../../services/shelter.service";
import {ShelterDetailDto} from "../../dtos/shelter";
import {ActivatedRoute, Router} from "@angular/router";



@Component({
  selector: 'app-shelter',
  templateUrl: './shelter.component.html',
  styleUrls: ['./shelter.component.scss']
})
export class ShelterComponent implements OnInit {
  protected animals: AnimalDto[] = [];
  protected shelter: ShelterDetailDto;
  private name: string;

  constructor(private shelterService: ShelterService,
              private router: Router,
              private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
      this.route.params.subscribe(params => {
        this.shelterService.findById(params['id']).subscribe(shelter => {
          this.shelter = shelter;
          this.animals = this.shelter.animals;
          this.name = this.shelter.name;
        });
      });
  }

  editAnimal(animalId: number): void {
    // Implement logic to edit the animal with the specified ID
    console.log('Editing animal with ID:', animalId);
  }

  onFilterChange(selectedFilter: string): void {

  }
}
