import {Component, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
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

  animal: AnimalDetailDto;

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
