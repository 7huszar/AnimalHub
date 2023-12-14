import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from "@angular/forms";
import {Mode} from "../../../enums/modes";
import {ShelterDetailDto, ShelterDto} from "../../../dtos/shelter";
import {ShelterService} from "../../../services/shelter.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CreateFileDto} from "../../../dtos/file";

@Component({
  selector: 'app-shelter-create-edit',
  templateUrl: './shelter-create-edit.component.html',
  styleUrls: ['./shelter-create-edit.component.scss']
})
export class ShelterCreateEditComponent implements OnInit{

  mode: Mode = Mode.CREATE;
  shelter: ShelterDto = {
    id: '',
    name: '',
    address: '',
    phoneNumber: '',
    email: '',
    createdOn: null
  }

  private shelterImage: File = null;
  protected shelterImageURL: any = null;

  constructor(private shelterService: ShelterService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data.mode;
      if (!this.modeIsCreate) {
        this.route.params.subscribe(params => {
          const id = params['id'];
          this.shelterService.findById(id).subscribe({
            next: (data: ShelterDetailDto) => {
              this.shelter = data;
              this.shelterService.getProfileImage(this.shelter.id).subscribe({
                next: blob => {
                  this.shelterImageURL = URL.createObjectURL(blob);
                }, error: err => {
                  console.log(err); // TODO handle
                }
              });
            }, error: err => {
              console.log('Error loading shelter: ', err); // TODO handle
            }
          });
        });
      }
    });
  }

  public onSubmit(form: NgForm): void {
    if (this.modeIsCreate) {
      this.shelterService.create(this.shelter).subscribe({
        next: shelterDto => {
          console.log('shelter created', shelterDto);
          if (this.shelterImage !== null) {
            const createFileDto: CreateFileDto = {file: this.shelterImage, name: this.shelterImage.name, summary: ""};
            this.shelterService.setProfileImage(shelterDto.id, createFileDto).subscribe({
              next: fileDto => {
                console.log('shelter profile image set', fileDto);
                this.navigateSuccess(shelterDto.id);
              }, error: err => {
                console.error(err); // TODO handle
              }
            });
          } else {
            this.navigateSuccess(shelterDto.id);
          }
        }, error: err => {
          console.error(err); // TODO handle
        }
      });
    } else {
      this.shelterService.update(this.shelter).subscribe({
        next: shelterDto => {
          console.log('shelter updated', shelterDto);
          if (this.shelterImage !== null) {
            const createFileDto: CreateFileDto = {file: this.shelterImage, name: this.shelterImage.name, summary: ""};
            this.shelterService.updateProfileImage(shelterDto.id, createFileDto).subscribe({
              next: fileDto => {
                console.log('shelter profile image updated', fileDto);
                this.navigateSuccess(shelterDto.id);
              }, error: err => {
                console.error(err); // TODO handle
              }
            })
          } else {
            this.navigateSuccess(shelterDto.id);
          }
        }, error: err => {
          console.error(err); // TODO handle
        }
      });
    }
  }

  public onDelete(): void {
    this.shelterService.delete(this.shelter.id).subscribe({
      next: data => {
        this.router.navigate(['/admin']);
      }, error: err => {
        console.error(err); // TODO handle
      }
    });
  }

  public onImageSelected(event: any): void {
    const img = event.target.files[0];
    if (img.size > 52428800) {
      console.error("File is too big!"); // TODO handle
    } else {
      this.shelterImage = img;
      this.shelterImageURL = URL.createObjectURL(this.shelterImage);
    }
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  private navigateSuccess(id: string): void {
    this.router.navigate(['/shelter/', id]);
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

  hasLoadedImg() :boolean {
    return this.shelterImageURL != null;
  }
}
