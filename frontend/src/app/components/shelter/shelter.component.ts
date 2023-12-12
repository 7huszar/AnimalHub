import {Component, OnInit} from '@angular/core';
import {AnimalDetailDto, AnimalDto} from "../../dtos/animal";
import  {ShelterService} from "../../services/shelter.service";
import {ShelterDetailDto} from "../../dtos/shelter";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";



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
              private route: ActivatedRoute,
              private notification: ToastrService
  ) {
  }

  ngOnInit(): void {
      this.route.params.subscribe(params => {
        this.shelterService.findById(params['id']).subscribe({next: shelter => {
          this.shelter = shelter;
          this.animals = this.shelter.animals;
          this.name = this.shelter.name;
          },
          error: error => {
            console.error('Error finding shelter', error);
            this.notification.error("Could Not find shelter");
          }
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
