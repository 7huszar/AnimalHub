import {Component, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Species} from "../../dtos/species";
import {ShelterDto} from "../../dtos/shelter";

@Component({
  selector: 'app-animal-homepage',
  templateUrl: './animal-homepage.component.html',
  styleUrls: ['./animal-homepage.component.scss']
})
export class AnimalHomepageComponent implements OnInit{

  animal: AnimalDetailDto = {
    birthDate: undefined,
    createdOn: undefined,
    height: 0,
    id: "",
    name: "",
    race: "",
    shelter: undefined,
    species: undefined,
    weight: 0
  };

  animalImageURL: any = null;

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
        let animalObservable: Observable<AnimalDetailDto> = this.service.findById(this.animal.id)
        animalObservable.subscribe({
          next: data => {
            this.animal = data;
            this.loadAnimalPic();
          },
          error: error => {
            console.error('Error finding animal', error);
            this.notification.error("Could Not find animal");
          }
        });
      }
    });
  }

  private loadAnimalPic(): void {
    this.service.getProfileImage(this.animal.id).subscribe({
      next: data => {
        console.log("loaded image ", data);
        this.animalImageURL = URL.createObjectURL(data);
      }, error: err => {
        console.error(err)
      }
    });
  }

}
