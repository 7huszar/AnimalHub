import {Component, OnInit} from '@angular/core';
import {Species} from "../../../dtos/species";
import {Animal} from "../../../dtos/animal";
import {AnimalService} from "../../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm, NgModel} from '@angular/forms';
import {Observable} from "rxjs";
import {FileService} from "../../../services/file.service";
import {CreateFile} from "../../../dtos/file";
import {forEach} from "lodash";

export enum AnimalCreateEditMode {
  create,
  edit,
}

@Component({
  selector: 'app-create-edit',
  templateUrl: './create-edit.component.html',
  styleUrls: ['./create-edit.component.scss']
})
export class CreateEditComponent implements OnInit {

  mode: AnimalCreateEditMode = AnimalCreateEditMode.create;
  animal: Animal = {
    name: '',
    species: Species.dog,
    race: '',
    birthDate: new Date()
  };

  private image: File = null;
  private heightSet: boolean = false;
  private weightSet: boolean = false;
  private birthDateSet: boolean = false;

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
      case AnimalCreateEditMode.create:
        return 'Create New Animal';
      case AnimalCreateEditMode.edit:
        return 'Edit Animal';
      default:
        return '?';
    }
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case AnimalCreateEditMode.create:
        return 'Create';
      case AnimalCreateEditMode.edit:
        return 'Update';
      default:
        return '?';
    }
  }

  get modeIsCreate(): boolean {
    return this.mode === AnimalCreateEditMode.create;
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
        case AnimalCreateEditMode.create:
          this.uploadImage();
          // observable = this.animalService.create(this.animal);
          break;
        case AnimalCreateEditMode.edit:
          observable = this.animalService.update(this.animal);
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
          this.router.navigate(['/animals']);
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
    }
  }

  uploadImage() {
    const createFile: CreateFile = {name: this.image.name, summary: "", file: this.image}
    this.fileService.create(createFile);
  }
}
