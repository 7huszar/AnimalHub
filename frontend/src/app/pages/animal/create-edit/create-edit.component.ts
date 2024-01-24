import {Component, OnInit} from '@angular/core';
import {AnimalService} from "../../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm, NgModel} from '@angular/forms';
import {Mode} from "../../../enums/modes";
import {IdOnlyShelter} from "../../../dtos/shelter";
import {AnimalDetailDto} from "../../../dtos/animal";
import {CreateFileDto} from "../../../dtos/file";
import {NotifierService} from "../../../services/notifier.service";
import {Globals} from "../../../global/globals";
import {now} from "lodash";

@Component({
  selector: 'app-create-edit',
  templateUrl: './create-edit.component.html',
  styleUrls: ['./create-edit.component.scss']
})
export class CreateEditComponent implements OnInit {

  mode: Mode = Mode.CREATE;

  animal: AnimalDetailDto = {
    id: null,
    name: null,
    gender: null,
    species: null,
    englishRace: null,
    germanRace: null,
    birthDate: null,
    shelter: null,
    height: null,
    weight: null,
    createdOn: null
  };

  typeForDeletion: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';

  dropZoneColor: string = 'text-primary';

  animalImageSelected: boolean = false;

  private animalImage: File = null;
  protected animalImageURL: any = null;

  today: String = new Date().toISOString().slice(0, 10);


  constructor(
    protected globals: Globals,
    private animalService: AnimalService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: NotifierService
  ) {
  }

  /* TODO maybe?
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
   */

  animalForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
    this.typeForDeletion = "Animal";
  }


  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data.mode;
      if (this.modeIsCreate) {
        this.route.params.subscribe(params => {
          const shelterId = params['shelterId'];
          this.animal.shelter = new IdOnlyShelter(shelterId);
        });
      } else {
        this.route.params.subscribe(params => {
          const id = params['id'];
          this.animalService.findById(id).subscribe({
            next: animalDetailDto => {
              this.animal = animalDetailDto;
              this.animalService.getProfileImage(this.animal.id).subscribe({
                next: blob => {
                  this.animalImageURL = URL.createObjectURL(blob);
                }, error: err => {
                  console.error(); // TODO handle
                }
              })
            }, error: err => {
              console.error(err); // TODO handle
            }
          });
        });
      }
    });
  }


  public onSubmit(form: NgForm): void {
    if (this.modeIsCreate) {
      this.animalService.create(this.animal).subscribe({
        next: animalDto => {
          console.log('animal created', animalDto);
          if (this.animalImage !== null) {
            const createFileDto: CreateFileDto = {file: this.animalImage, name: this.animalImage.name, summary: "TODO make summary"};
            this.animalService.setProfileImage(animalDto.id, createFileDto).subscribe({
              next: fileDto => {
                console.log('animal profile image set', fileDto);
                this.navigateSuccess(animalDto.id);
              }, error: err => {
                console.error(err); // TODO handle
              }
            });
          } else {
            this.navigateSuccess(animalDto.id);
          }
        }, error: err => {
          console.error(err); // TODO handle
        }
      });
    } else {
      this.animalService.update(this.animal).subscribe({
        next: animalDto => {
          console.log('animal updated', animalDto);
          if (this.animalImage !== null) {
            const createFileDto: CreateFileDto = {file: this.animalImage, name: this.animalImage.name, summary: ''};
            this.animalService.updateProfileImage(animalDto.id, createFileDto).subscribe({
              next: fileDto => {
                console.log('animal profile image updated', fileDto);
                this.navigateSuccess(animalDto.id);
              }, error: err => {
                console.error(err); // TODO handle
              }
            });
          } else {
            this.navigateSuccess(animalDto.id);
          }
        }, error: err => {
          console.error(err); // TODO handle
        }
      });
    }
  }


  private navigateSuccess(id: string): void {
    this.router.navigate(['/animal/', id]);
  }

  public onImageSelected(event: any): void {
      const img = event.target.files[0];
      this.animalImageSelected = true;
      this.setImage(img);
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  get modeIsCreate(): boolean {
    return this.mode === Mode.CREATE;
  }

  get submitButtonText(): string {
    if (this.modeIsCreate) {
      return 'Create';
    }
    return 'Update';
  }

  public dropped(event: any) :void {
    event.preventDefault();
    event.stopPropagation();
    if (this.animalImageURL) {
      return;
    }
    if (event?.dataTransfer?.items?.length > 0 && event.dataTransfer.items[0].kind === 'file') {
      this.setImage(event.dataTransfer.items[0].getAsFile());
      this.animalImageSelected = true;
    } else {
      this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
    }
  }

  public enterDropZone(event: any) :void {
    event.preventDefault();
    event.stopPropagation();
    this.dropZoneColor = 'text-secondary';
  }

  public leaveDropZone(event: any) :void {
    event.preventDefault();
    event.stopPropagation();
    this.dropZoneColor = 'text-primary';
  }


  /**
   * Sets profile picture for animal.
   * Max: 42.4288 MB, doesn't check whether img is null.
   *
   */
  private setImage(img: File): void {
    console.log(img);
    // 52,4288 MB?
    if (img.size > 52428800) {
      console.error("File is too big!"); // TODO handle
    } else {
      this.animalImage = img;
      this.animalImageURL = URL.createObjectURL(this.animalImage);
    }
  }
}
