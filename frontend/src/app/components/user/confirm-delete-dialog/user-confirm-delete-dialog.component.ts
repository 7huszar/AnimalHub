import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {UserAdminPageComponent} from "../../admin-page/user-admin-page/user-admin-page.component";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'user-app-confirm-delete-dialog',
  templateUrl: './user-confirm-delete-dialog.component.html',
  styleUrls: ['./user-confirm-delete-dialog.component.scss'],
})

export class UserConfirmDeleteDialogComponent {

  @Input() deleteWhat: string | undefined = '';
  @Input() DeleteId: string | undefined = '';
  @Input() Mode: UserDeleteMode | undefined;

  @Output() confirm = new EventEmitter<void>();
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
          error: error => {
            console.error('Error deleting user', error);
            this.notification.error(error.error.message, error.error.error);
          }
        });
    }
  }
}

export enum UserDeleteMode {
  edit,
  delete
}
