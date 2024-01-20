import {Component} from '@angular/core';
import {ShelterService} from "../../services/shelter.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotifierService} from "../../services/notifier.service";
import {ShelterDto} from "../../dtos/shelter";
import {UserDetailDto} from "../../dtos/user";
import {CreateFileDto} from "../../dtos/file";
import {NgForm, NgModel} from "@angular/forms";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-member.edit.shelter',
  templateUrl: './member.edit.shelter.component.html',
  styleUrls: ['./member.edit.shelter.component.scss']
})
export class MemberEditShelterComponent {


  dropZoneColor: string = 'text-primary';

  private shelterImage: File = null;
  protected shelterImageURL: any = null;


  shelter: ShelterDto = {
    paypalClientId: "",
    paypalSecret: "",
    donationId: '',
    subscriptionId: '',
    id: '',
    name: '',
    address: '',
    phoneNumber: '',
    email: '',
    createdOn: null
  }

  user: UserDetailDto = {
    email: "",
    id: "",
    role: ""
  }

  constructor(private shelterService: ShelterService,
              private router: Router,
              private notification: NotifierService,
              private userService: UserService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {

      this.route.params.subscribe(params => {
        const id = params['id'];
        this.shelterService.findById(id).subscribe({
          next: shelterDetailDto => {
            this.shelter = shelterDetailDto;
            this.shelterService.getProfileImage(this.shelter.id).subscribe({
              next: blob => {
                this.shelterImageURL = URL.createObjectURL(blob);
              }, error: err => {
                console.error(err);
                const errorMessage = err.error ? err.error.message : 'Unknown error';
                this.notification.notifyError(errorMessage, "Error loading Profile Picture");
              }
            });
          }, error: err => {
            console.error('Error loading shelter: ', err);
            const errorMessage = err.error ? err.error.message : 'Unknown error';
            this.notification.notifyError(errorMessage, "Error loading Shelter");
          }
        });
      });

    });
  }

  public onSubmit(form: NgForm): void {

    if (this.shelterImage !== null) {
      const createFileDto: CreateFileDto = {file: this.shelterImage, name: this.shelterImage.name, summary: this.shelterImage.name};
      this.shelterService.updateProfileImage(this.shelter.id, createFileDto).subscribe({
        next: fileDto => {
          this.notification.notifySuccess("Successfully updated Profile Picture");
          console.log('shelter profile image updated', fileDto);
          this.navigateSuccess();
        }, error: err => {
          console.error(err);
          const errorMessage = err.error ? err.error.message : 'Unknown error';
          this.notification.notifyError(errorMessage, "Error updating Paypal Credentials");
        }
      })
    }

    this.userService.updatePaypalDetails(this.shelter).subscribe({
      next: () => {
        this.notification.notifySuccess("Successfully Updated ShelterDetails");
        this.navigateSuccess()

      }, error: err => {
        console.error('Error updating Paypal Credentials:', err);

        const errorMessage = err.error ? err.error.message : 'Unknown error';
        this.notification.notifyError(errorMessage, "Error updating Paypal Credentials");
      }
    });

  }

  private navigateSuccess(): void {
    this.router.navigate(['/home']);
  }

  public onImageSelected(event: any): void {
    const img = event.target.files[0];
    this.setImage(img);
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }


  get submitButtonText(): string {
    return 'Update';
  }

  public dropped(event: any): void {
    event.preventDefault();
    event.stopPropagation();
    if (this.shelterImageURL) {
      return;
    }
    if (event?.dataTransfer?.items?.length > 0 && event.dataTransfer.items[0].kind === 'file') {
      this.setImage(event.dataTransfer.items[0].getAsFile());
    } else {
      this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
    }
  }

  public enterDropZone(event: any): void {
    event.preventDefault();
    event.stopPropagation();
    this.dropZoneColor = 'text-secondary';
  }

  public leaveDropZone(event: any): void {
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
    // 52,4288 MB?
    if (img.size > 52428800) {
      console.error("File is too big!"); // TODO handle
    } else {
      this.shelterImage = img;
      this.shelterImageURL = URL.createObjectURL(this.shelterImage);
    }
  }

}
