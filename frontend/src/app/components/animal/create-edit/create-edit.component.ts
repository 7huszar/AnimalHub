import {Component, OnInit} from '@angular/core';
import {Species} from "../../../dtos/species";
import {Animal} from "../../../dtos/animal";
import {AnimalService} from "../../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm, NgModel} from '@angular/forms';
import {Observable} from "rxjs";
import {FileService} from "../../../services/file.service";
import {CreateAnimalFile} from "../../../dtos/file";
import {Mode} from "../../../enums/modes";
import {IdOnlyShelter} from "../../../dtos/shelter";

@Component({
  selector: 'app-create-edit',
  templateUrl: './create-edit.component.html',
  styleUrls: ['./create-edit.component.scss']
})
export class CreateEditComponent implements OnInit {

  mode: Mode = Mode.CREATE;
  animal: Animal = {
    name: '',
    species: Species.dog,
    race: '',
    birthDate: new Date(),
    height: 0,
    weight: 0,
    shelter: new IdOnlyShelter('970846d3-aeba-4ce9-9057-3e222137a692')
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
    private fileService: FileService,
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

        this.route.params.subscribe(params => {
          this.animalService.findById(this.route.snapshot.paramMap.get('id')!).subscribe({
            next: (data: Animal) => {
              if (data) {
                this.animal.id = data.id;
                this.animal.name = data.name;
                this.animal.species = data.species;
                this.animal.race = data.race;
                this.animal.birthDate = data.birthDate;
                this.animal.weight = data.weight;
                this.animal.height = data.height;
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
    if (form.valid) {
      let observable: Observable<Animal>;
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
    const createFile: CreateAnimalFile = {name: this.image.name, summary: "", file: this.image, animalId: id}
    this.fileService.create(createFile);
  }
}
