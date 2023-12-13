import {Component, OnInit} from '@angular/core';
import {Species} from "../../../dtos/species";
import {AnimalService} from "../../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm, NgModel} from '@angular/forms';
import {Observable} from "rxjs";
import {Mode} from "../../../enums/modes";
import {IdOnlyShelter} from "../../../dtos/shelter";
import {AnimalDetailDto, CreateAnimalDto} from "../../../dtos/animal";
import {CreateFileDto} from "../../../dtos/file";

@Component({
  selector: 'app-create-edit',
  templateUrl: './create-edit.component.html',
  styleUrls: ['./create-edit.component.scss']
})
export class CreateEditComponent implements OnInit {

  mode: Mode = Mode.CREATE;

  // TODO here would be a AnimalDetailDto also needed for Mode.EDIT, maybe use AnimalDetailDto always with empty string id for create and make this into a CreateAnimalDto for CREATE
  animal: CreateAnimalDto = {
    name: '',
    species: Species.dog,
    race: '',
    birthDate: new Date(),
    height: 0,
    weight: 0,
    shelter: new IdOnlyShelter('f78daf38-cd64-48c9-8c9a-44c13f8eafa3')
  };

  private image: File = null;
  private heightSet: boolean = false;
  private weightSet: boolean = false;
  private birthDateSet: boolean = false;
  private profilePictureChanged: boolean = false;

  today: String = new Date().toISOString().slice(0, 10);


  get height(): number | null {
    return this.heightSet
      ? this.animal.height
      : null;
  }

  set height(value: number) {
    this.heightSet = true;
    this.animal.height = value;
  }

  get weight(): number | null {
    return this.weightSet
      ? this.animal.weight
      : null;
  }

  set weight(value: number) {
    this.weightSet = true;
    this.animal.weight = value;
  }

  get birthDate(): Date | null {
    return this.birthDateSet
      ? this.animal.birthDate
      : null;
  }

  set birthDate(value: Date) {
    this.birthDateSet = true;
    this.animal.birthDate = value;
  }


  constructor(
    private animalService: AnimalService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  public get heading(): string {
    switch (this.mode) {
      case Mode.CREATE:
        return 'Create New Animal';
      case Mode.EDIT:
        return 'Edit Animal';
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case Mode.CREATE:
        return 'Create';
      case Mode.EDIT:
        return 'Update';
      default:
        return '?';
    }
  }

  get modeIsCreate(): boolean {
    return this.mode === Mode.CREATE;
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data.mode;
      if (!this.modeIsCreate) {
        this.birthDateSet = true;
        this.weightSet = true;
        this.heightSet = true;

        // TODO FIX

        this.route.params.subscribe(params => {
          this.animalService.findById(this.route.snapshot.paramMap.get('id')!).subscribe({
            next: (data: AnimalDetailDto) => {
              if (data) {
                this.animal = data;
              }
            },
            error: (error) => {
              console.error('Error loading animal data:', error);
            }
          });
        });
      }
    });
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public onSubmit(form: NgForm): void {
    console.log('is form valid?', form.valid, this.animal);
    // TODO FIX

    if (form.valid) {
      let observable: Observable<AnimalDetailDto>;
      switch (this.mode) {
        case Mode.CREATE:
          this.animalService.create(this.animal).subscribe({
            next: data => {
              this.uploadImage(data.id);
              this.router.navigate(['/home']);
            }
          });

          break;
        case Mode.EDIT:
          // this.animalService.update(this.animal);
          break;
        default:
          console.error('Unknown AnimalCreateEditMode', this.mode);
          return;
      }
      // observable.subscribe({
      //   next: data => {
      //     this.router.navigate(['/animals']);
      //   },
      //   error: error => {
      //     console.error('Error creating animal', error);
      //   }
      // });
    }

  }

  public onDelete() {
    this.animalService.delete(this.route.snapshot.paramMap.get('id')!).subscribe({
        next: data => {
          this.router.navigate(['/home']);
        }, error: error => {
          console.error('Error deleting animal', error);
        }
      }
    );
  }

  onImageSelected(event: any): void {
    if (event.target.files[0].size > 52428800) {
      alert("File is too big!");
    } else {
      this.image = event.target.files[0];
      this.profilePictureChanged = true;
    }
  }

  uploadImage(id: string) {
    // TODO FIX
    console.log("uploading image with animal id ", id);

    const createFile: CreateFileDto = {name: this.image.name, summary: "", file: this.image}
    this.animalService.setProfileImage(id, createFile).subscribe({
      next: data => {
        console.log(data)
      }, error: err => {
        console.log(err)
      }
    });

  }
}
