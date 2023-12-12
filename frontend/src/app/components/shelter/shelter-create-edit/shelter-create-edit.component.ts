import {Component, OnInit} from '@angular/core';
import {NgForm, NgModel} from "@angular/forms";
import {Mode} from "../../../enums/modes";
import {Shelter} from "../../../dtos/shelter";
import {ShelterService} from "../../../services/shelter.service";
import {FileService} from "../../../services/file.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-shelter-create-edit',
  templateUrl: './shelter-create-edit.component.html',
  styleUrls: ['./shelter-create-edit.component.scss']
})
export class ShelterCreateEditComponent implements OnInit{

  mode: Mode = Mode.CREATE;
  shelter: Shelter = {
    name: '',
    address: '',
    phoneNumber: '',
    email: ''
  }

  private shelterImage: File = null;
  protected shelterImageURL: any = null;
  private shelterImageChanged: boolean = false;

  constructor(private shelterService: ShelterService,
              private fileService: FileService,
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
            next: (data: Shelter) => {
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
          // TODO upload file
          this.router.navigate(['/home']);
        }
      });
    } else {
      this.shelterService.update(this.shelter).subscribe({
        next: data => {
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
}
