import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ShelterService} from "../../services/shelter.service";
import {UserService} from "../../services/user.service";
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../services/notifier.service";
import {Router} from "@angular/router";
import {AnimalService} from "../../services/animal.service";
import {Observable} from "rxjs";
import {PaypalService} from "../../services/paypal.service";


@Component({
  selector: 'app-confirm-delete-dialog',
  templateUrl: './confirm-delete-dialog.component.html',
  styleUrls: ['./confirm-delete-dialog.component.scss'],
})

export class ConfirmDeleteDialogComponent {

  @Output() deleteErrorEvent = new EventEmitter<{ error: string | null, errorMessage: string }>();

  @Input() deleteName: string | undefined = '';
  @Input() deleteType: string | undefined = '';
  @Input() DeleteId: string | undefined = '';
  @Input() cancelShelter: string | undefined;


  @HostBinding('class') cssClass = 'modal-fade';

  constructor(
    private shelterService: ShelterService,
    private animalService: AnimalService,
    private paypalService: PaypalService,
    private notification :NotifierService,
    private userService: UserService,
    private router: Router,

  ) {
  }

  public deleteShelter() {
    if (this.DeleteId != null) {
      console.log(this.DeleteId)

      this.shelterService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.notifySuccess(`Shelter ${this.deleteName} successfully deleted.`);
            this.reloadAdmin();
          },
          error: error => {
            console.error('Error deleting Shelter', error);
            this.notification.notifyError(error.error.message, error.error.error);
          }
        });
    }
  }


  reloadAdmin() {
    this.router.navigateByUrl('/admin', {skipLocationChange: true}).then(() => {
      window.location.reload();
    });
  }



  reloadSubscriptions() {
    this.router.navigateByUrl('/users/subscriptions', {skipLocationChange: true}).then(() => {
      window.location.reload();
    });
  }

  public deleteUser() {
    if (this.DeleteId != null) {
      this.userService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.notifySuccess(`User ${this.deleteName} successfully deleted.`);
            this.reloadAdmin();
          },
          error: error => {
            console.error('Error deleting user', error);
            const errorMessage = error.error ? error.error.message : 'Unknown error';
            this.notification.notifyError(errorMessage, "Error deleting User");
          }

        });
    }
  }


  public deleteAnimal() {
    if (this.DeleteId != null) {
      this.animalService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.notifySuccess(`Animal ${this.deleteName} successfully deleted.`);
          },
          error: error => {
            console.error('Error deleting animal', error);
            this.notification.notifyError(error.error.message, error.error.error);
          }
        });
    }
  }

  public cancelSubscription() {

    if (this.DeleteId != null) {
      let test: Observable<void> = this.paypalService.cancelSubscriptions(this.DeleteId, this.cancelShelter)
      test.subscribe({
        next: data => {
          this.notification.notifySuccess(`Subscription to ${this.deleteName} successfully canceled.`);
          this.reloadSubscriptions();
        },
        error: error => {
          //this schould never happen except if no subscriptions have been made
          console.error('Error posting endpoint', error);
        }
      });
    }
  }

  public delete() {
    if (this.deleteType === 'User') {
      this.deleteUser()
    } else if(this.deleteType === 'Shelter') {
      this.deleteShelter()
    }else if (this.deleteType === 'Animal'){
      this.deleteAnimal()
    }else if (this.deleteType === 'Subscription'){
      this.cancelSubscription()
    }
    window.location.reload();
  }
}





