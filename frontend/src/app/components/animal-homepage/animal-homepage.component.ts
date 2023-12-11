import {Component, OnInit} from '@angular/core';
import {Animal} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-animal-homepage',
  templateUrl: './animal-homepage.component.html',
  styleUrls: ['./animal-homepage.component.scss']
})
export class AnimalHomepageComponent implements OnInit{

  animal: Animal = {
    birthDate: undefined,
    createdOn: undefined,
    height: 0,
    weight: 0,
    sex: "",
    id: "",
    name: "",
    race: "",
    shelter: undefined,
    species: undefined,
  }

  constructor(
    private service: AnimalService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if(params.get("id")) {
        this.animal.id = params.get("id");
        let animalObservable: Observable<Animal> = this.service.findById(this.animal.id)
        animalObservable.subscribe({
          next: data => {
            this.animal.name = data.name;
            this.animal.sex = data.sex;
            this.animal.height = data.height;
            this.animal.weight = data.weight;
            this.animal.birthDate = data.birthDate;
            this.animal.race = data.race;
            this.animal.species = data.species;
            this.animal.shelter = data.shelter;
          },
          error: error => {
            console.error('Error finding animal', error);
            this.notification.error("Could Not find animal");
          }
        });
      }
    });
  }

}
