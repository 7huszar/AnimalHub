import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {UserAdminPageComponent} from "../../admin-page/user-admin-page/user-admin-page.component";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'app-user-confirm-delete-dialog', // Change to a unique selector
  templateUrl: './user-confirm-delete-dialog.component.html',
  styleUrls: ['./user-confirm-delete-dialog.component.scss'],
})

export class UserConfirmDeleteDialogComponent {

  public deleteError: string | null = null;
  @Output() deleteErrorEvent = new EventEmitter<{ error: string | null, errorMessage: string }>();

  @Input() deleteWhat: string | undefined = '';
  @Input() DeleteId: string | undefined = '';
  @Input() Mode: UserDeleteMode | undefined;

  @HostBinding('class') cssClass = 'modal-fade';

  constructor(
    private service: UserService,
    private notification: ToastrService,
    private adminPageComponent: UserAdminPageComponent,
  ) {
  }

  public deleteUser() {
    if (this.DeleteId != null) {
      this.service.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.success(`User ${this.deleteWhat} successfully deleted.`);
            switch (this.Mode) {
              case UserDeleteMode.delete:
                break;
              case UserDeleteMode.edit:
                this.adminPageComponent.reloadUsers();
                break;
            }
          },
          error: (error) => {
            console.error('Error deleting user', error);

            if (error.status === 409) {
              // Conflict error
              this.deleteError = error.error || 'Conflict error occurred during deletion.';
            } else {
              this.deleteError = 'An error occurred during deletion.';
            }

            this.deleteErrorEvent.emit({ error: this.deleteError, errorMessage: this.deleteError || '' });
          },
        });
    }
  }

}
  export enum UserDeleteMode {
  edit,
  delete
}
