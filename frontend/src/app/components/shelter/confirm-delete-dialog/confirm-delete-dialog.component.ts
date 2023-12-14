import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {ShelterService} from "../../../services/shelter.service";
import {AdminPageComponent} from "../../admin-page/shelter-admin-page/admin-page.component";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-confirm-delete-dialog',
  templateUrl: './confirm-delete-dialog.component.html',
  styleUrls: ['./confirm-delete-dialog.component.scss'],
})

export class ConfirmDeleteDialogComponent {

  public deleteError: string | null = null;
  @Output() deleteErrorEvent = new EventEmitter<{ error: string | null, errorMessage: string }>();


  @Input() deleteWhat: string | undefined = '';
  @Input() deleteType: string | undefined = '';
  @Input() DeleteId: string | undefined = '';
  @Input() Mode: entityDeleteMode | undefined;

  @Output() confirm = new EventEmitter<void>();
  @HostBinding('class') cssClass = 'modal-fade';

  constructor(
    private service: ShelterService,
    private notification: ToastrService,
    private adminPageComponent: AdminPageComponent,
    private userService: UserService,
  ) {
  }


  public deleteShelter() {
    if (this.DeleteId != null) {

      this.service.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.success(`Shelter ${this.deleteWhat} successfully deleted.`);
            this.adminPageComponent.reloadShelters();
          },
          error: error => {
            console.error('Error deleting shelter', error);
            this.notification.error(error.error.message, error.error.error);
          }
        });
    }
  }

  public deleteUser() {
    if (this.DeleteId != null) {
      this.userService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.success(`User ${this.deleteWhat} successfully deleted.`);
            this.adminPageComponent.reloadUsers();
          },
          error: (error) => {
            console.error('Error deleting user', error);

            if (error.status === 409) {
              // Conflict error
              this.deleteError = error.error || 'Conflict error occurred during deletion.';
            } else {
              this.deleteError = 'An error occurred during deletion.';
            }

            this.deleteErrorEvent.emit({error: this.deleteError, errorMessage: this.deleteError || ''});
          },
        });
    }
  }


  public deleteEntity() {
    console.log("kek")
    console.log(this.deleteWhat)
    console.log(this.DeleteId)
    switch (this.Mode) {
      case entityDeleteMode.user:
        this.deleteUser()
        break;
      case entityDeleteMode.shelter:
        this.deleteShelter()
        break;
    }
  }
}

export enum entityDeleteMode {
  user,
  shelter
}


