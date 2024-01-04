import {Component, OnInit} from '@angular/core';
import {AnimalDto} from "../../dtos/animal";
import  {ShelterService} from "../../services/shelter.service";
import {ShelterDetailDto} from "../../dtos/shelter";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Mode} from "../../enums/modes";
import { Location } from '@angular/common';



@Component({
  selector: 'app-shelter',
  templateUrl: './shelter.component.html',
  styleUrls: ['./shelter.component.scss']
})
export class ShelterComponent implements OnInit {
  protected animals: AnimalDto[] = [];
  protected shelter: ShelterDetailDto;

  constructor(private shelterService: ShelterService,
              private route: ActivatedRoute,
              private notification: ToastrService,
              private location: Location,
) {
  }

  goBack(): void {
    this.location.back();
  }

  ngOnInit(): void {
      this.route.params.subscribe(params => {

        this.shelterService.findById(params['id']).subscribe({next: shelter => {
          this.shelter = shelter;
          this.animals = this.shelter.animals;
          },
          error: error => {
            console.error('Error finding shelter', error);
            this.notification.error("Could Not find shelter");
          }
        });
      });
  }

  protected readonly Mode = Mode;
}
