import {Component, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Mode} from "../../enums/modes";
import {CreateFileDto, FileDto} from "../../dtos/file";
import {AuthService} from "../../services/auth.service";

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
    weight: 0,
  };
  // TODO: add fitting animal attributes
  protected animalAge: number;

  protected contents: FileDto[];
  protected newContent: File = null;
  constructor(
      protected authService: AuthService,
    private service: AnimalService,
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
            this.calculateAge(this.animal.birthDate)
          },
          error: error => {
            console.error('Error finding animal', error);
            this.notification.error("Could Not find animal");
          }
        });
      }
    });
    this.service.getContents(this.animal.id).subscribe({
      next: data => {
        this.contents = data;
      },
      error: error => {
        console.error('Error finding contents', error);
        this.notification.error("Could Not find contents of animal");
      }
    })
  }

  public onNewContentSelected(event: any): void {
    if (event.target.files[0]) {
      this.newContent = event.target.files[0];
    }
  }

  public onUpload(): void {
    const createFileDto: CreateFileDto = {file: this.newContent, name: this.newContent.name, summary: ""};
    this.service.addContent(this.animal.id, createFileDto).subscribe({
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

    let age = today.getFullYear() - birthdateDate.getFullYear();

    // Check if the birthday has occurred this year
    const todayMonth = today.getMonth();
    const birthdateMonth = birthdateDate.getMonth();

    if (todayMonth < birthdateMonth || (todayMonth === birthdateMonth && today.getDate() < birthdateDate.getDate())) {
      age--;
    }
    this.animalAge = age;
  }

  protected canUpload(): boolean {
    return this.authService.isAdmin() || (this.animal.shelter !== null && this.authService.isMemberOf(this.animal.shelter.id));
  }

  protected readonly Mode = Mode;
}
