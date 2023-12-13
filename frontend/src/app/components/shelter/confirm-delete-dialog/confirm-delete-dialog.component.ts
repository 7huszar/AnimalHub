import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {ShelterService} from "../../../services/shelter.service";
import {AdminPageComponent} from "../../admin-page/shelter-admin-page/admin-page.component";

@Component({
  selector: 'app-confirm-delete-dialog',
  templateUrl: './confirm-delete-dialog.component.html',
  styleUrls: ['./confirm-delete-dialog.component.scss'],
})

export class ConfirmDeleteDialogComponent {

  @Input() deleteWhat: string | undefined = '';
  @Input() DeleteId: string | undefined = '';
  @Input() Mode: ShelterDeleteMode | undefined;

  @Output() confirm = new EventEmitter<void>();
  @HostBinding('class') cssClass = 'modal-fade';

  constructor(
    private service: ShelterService,
    private notification: ToastrService,
    private adminPageComponent: AdminPageComponent,
  ) {
  }

  public deleteShelter() {
    if (this.DeleteId != null) {

      this.service.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.success(`Shelter ${this.deleteWhat} successfully deleted.`);
            switch (this.Mode) {
              case ShelterDeleteMode.delete:
                break;
              case ShelterDeleteMode.edit:
                this.adminPageComponent.reloadShelters();
                break;
            }
          },
          error: error => {
            console.error('Error deleting shelter', error);
            this.notification.error(error.error.message, error.error.error);
          }
        });
    }
  }
}

export enum ShelterDeleteMode {
  edit,
  delete
}
