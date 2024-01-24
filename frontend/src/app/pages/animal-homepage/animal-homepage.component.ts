import {Component, ElementRef, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Mode} from "../../enums/modes";
import {CreateFileDto, FileDto} from "../../dtos/file";
import {AuthService} from "../../services/auth.service";
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto} from "../../dtos/user";
import {UserService} from "../../services/user.service";
import {Species} from "../../dtos/species";
import {PaypalService} from "../../services/paypal.service";
import {Gender} from "../../dtos/gender";
import {Globals} from "../../global/globals";
import {NotifierService} from "../../services/notifier.service";



@Component({
  selector: 'app-animal-homepage',
  templateUrl: './animal-homepage.component.html',
  styleUrls: ['./animal-homepage.component.scss']
})
export class AnimalHomepageComponent implements OnInit {

  animal: AnimalDetailDto;

  user: UserDetailDto = {
    email: "",
    id: "",
    role: ""
  };

  protected animalAge: number;
  protected contents: FileDto[];
  protected newContent: File = null;

  protected years: boolean;
  protected months: boolean;
  protected days: boolean;
  protected year: boolean;
  protected month: boolean;
  protected day: boolean;

  protected isDog: boolean;
  protected isCat: boolean;
  protected isBird: boolean;
  protected isRodent: boolean;


  constructor(
    protected globals: Globals,
    protected authService: AuthService,
    private animalService: AnimalService,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if (params.get("id")) {

        const id = params.get("id");

        let animalObservable: Observable<AnimalDetailDto> = this.animalService.findById(id)
        animalObservable.subscribe({
          next: data => {
            this.animal = data;
            console.log(this.animal.shelter.subscriptionId)
            this.calculateAge(this.animal.birthDate)

            switch (this.animal.species) {
              case this.animal.species = Species.cat: {
                this.isCat = true;
                break;
              }
              case this.animal.species = Species.dog: {
                this.isDog = true;
                break;
              }
              case this.animal.species = Species.bird: {
                this.isBird = true;
                break;
              }
              case this.animal.species = Species.rodent: {
                this.isRodent = true;
                break;
              }
            }

            this.animalService.getContents(this.animal.id).subscribe({
              next: data => {
                this.contents = data;
              },
              error: error => {
                console.error('Error finding contents', error);
                this.notification.error("Could Not find contents of animal");
              }
            })
          },
          error: error => {
            console.error('Error finding animal', error);
            this.notification.error("Could Not find animal");
          }
        });
      }
    });
  }

  public onNewContentSelected(event: any): void {
    if (event.target.files[0]) {
      this.newContent = event.target.files[0];
    }
  }

  public onUpload(): void {
    const createFileDto: CreateFileDto = {
      file: this.newContent,
      name: this.newContent.name,
      summary: "TODO make summary"
    };
    this.animalService.addContent(this.animal.id, createFileDto).subscribe({
      next: fileDto => {
        console.log(fileDto);
        this.newContent = null;
        this.contents.unshift(fileDto);
      },
      error: err => {
        console.error(err); // TODO handle
      }
    });
  }

  public calculateAge(birthdate: Date): void {
    const today = new Date();
    const birthdateDate = new Date(birthdate);

    // Convert the milliseconds to years
    let timeDifference = (today.getTime() - birthdateDate.getTime()) / (24 * 60 * 60 * 1000);
    let age = timeDifference;
    console.log("AGE " + timeDifference);
    if (timeDifference > 365.25) {
      age = Math.floor(timeDifference / 365.25);
      if (age > 1) {
        this.years = true;
      } else {
        this.year = true;
      }
    }
    if (timeDifference < 365.25 && timeDifference > 30) {
      age = Math.floor(timeDifference / (30));
      if (age > 1) {
        this.months = true;
      } else {
        this.month = true;
      }
    }
    if (30 > timeDifference) {
      age = Math.floor(timeDifference);
      if (age > 1) {
        this.days = true;
      } else {
        this.day = true;
      }
    }
    this.animalAge = age;
  }

  protected canUpload(): boolean {
    return this.authService.isAdmin() || (this.animal.shelter && this.authService.isMemberOf(this.animal.shelter.id));
  }

  protected readonly Mode = Mode;
  protected readonly Gender = Gender;
}
