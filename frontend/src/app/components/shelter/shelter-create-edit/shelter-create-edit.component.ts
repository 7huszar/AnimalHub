import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from "@angular/forms";
import {Mode} from "../../../enums/modes";
import {ShelterDto} from "../../../dtos/shelter";
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
  private shelterImageChanged: boolean = false;

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
            next: (data: ShelterDto) => {
              this.shelter = data;
            }, error: (error) => {
              alert('Error loading shelter!');
              console.log('Error loading shelter: ', error);
            }
          });
        });
      }
    });
  }

  public onSubmit(form: NgForm): void {
    if (this.modeIsCreate) {
      this.shelterService.create(this.shelter).subscribe({
        next: data => {
          this.uploadImage(data.id, this.shelterImage);
          this.router.navigate(['/home']);
        }
      });
    } else {
      this.shelterService.update(this.shelter).subscribe({
        next: data => {
          // TODO update image();
          this.router.navigate(['/home']);
        }
      });
    }
  }

  public onDelete(): void {
    this.shelterService.delete(this.shelter.id).subscribe({
      next: data => {
        this.router.navigate(['/home']);
      }
    });
  }

  public onImageSelected(event: any): void {
    if (this.setImage(event.target.files[0])) {
      this.shelterImageChanged = true;
    }
  }

  private setImage(img: File): boolean {
    if (img.size > 52428800) {
      alert("File is too big!");
      return false;
    }

    this.shelterImage = img;
    this.shelterImageURL = URL.createObjectURL(this.shelterImage);
    return true;
  }

  private uploadImage(id: string, file: File): void {
    const createFileDto: CreateFileDto = {file: file, name: file.name, summary: ""};
    this.shelterService.setProfileImage(id, createFileDto).subscribe({
      next: data => {
        console.log('shelter profile picture set', data);
      }, error: err => {
        console.error(err);
      }
    });
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

  hasLoadedImg() :boolean {
    return this.shelterImageChanged;
  }

}
