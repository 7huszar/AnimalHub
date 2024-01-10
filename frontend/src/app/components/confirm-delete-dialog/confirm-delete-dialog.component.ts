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

  @Input() deleteWhat: string | undefined = '';
  @Input() deleteType: string | undefined = '';
  @Input() DeleteId: string | undefined = '';


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
            this.notification.notifySuccess(`Animal ${this.deleteWhat} successfully deleted.`);
            this.reloadAdmin();
          },
          error: error => {
            console.error('Error deleting Animal', error);
            this.notification.notifyError(error.error.message, error.error.error);
          }
        });
    }
  }


  reloadAdmin() {
    this.router.navigateByUrl('/admin', { skipLocationChange: true }).then(() => {
      window.location.reload();
    });
  }


  reloadAnimal() {
    this.router.navigate(['/shelter/' + this.DeleteId])
  }

  reloadSubscriptions() {
    this.router.navigateByUrl('/users/subscriptions', { skipLocationChange: true }).then(() => {
      window.location.reload();
    });
  }

  public deleteUser() {
    if (this.DeleteId != null) {
      this.userService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.notifySuccess(`User ${this.deleteWhat} successfully deleted.`);
            this.reloadAdmin();
          },
          error: error => {
            console.error('Error deleting user', error);
            let title: string = 'ADMIN_PAGE.FAILED';

            // status == 401 / Unauthorized - deleting admin not allowed
            if (error.status == HttpStatusCode.Unauthorized) {
              this.notification.notifyError('ADMIN_PAGE.INFO', title);
            } else {
              // standard errors, we do not anticpate
              this.notification.notifyStdError(error, title);
            }
          }

        });
    }
  }


  public deleteAnimal() {
    if (this.DeleteId != null) {

      this.animalService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.notifySuccess(`Shelter ${this.deleteWhat} successfully deleted.`);
            this.reloadAnimal();
            //this.adminPageComponent.reloadShelters();
          },
          error: error => {
            console.error('Error deleting shelter', error);
            this.notification.notifyError(error.error.message, error.error.error);
          }
        });
    }
  }

  //This is just for testing purposes use it
  public checkSubscription(){

    let test: Observable<void> = this.paypalService.checkSubscription(this.DeleteId)
    test.subscribe({
      next: data => {
        this.notification.notifySuccess(`Subscription to ${this.deleteWhat} successfully canceled.`);
        this.reloadSubscriptions();
      },
      error: error => {
        //this schould never happen except if no subscriptions have been made
        console.error('Error posting endpoint', error);
      }
    });

  }


  public cancelSubscription() {

  if (this.DeleteId != null) {

    let test: Observable<void> = this.paypalService.cancelSubscriptions(this.DeleteId)
    test.subscribe({
      next: data => {
        this.notification.notifySuccess(`Subscription to ${this.deleteWhat} successfully canceled.`);
        this.reloadSubscriptions();
      },
      error: error => {
        //this schould never happen except if no subscriptions have been made
        console.error('Error posting endpoint', error);
      }
    });
  }
}


public deleteEntity() {
  if (this.deleteType === 'User') {
    this.deleteUser()
  } else if(this.deleteType === 'Shelter') {
    this.deleteShelter()
  }else if (this.deleteType === 'Animal'){
    this.deleteAnimal()
  }else if (this.deleteType === 'Subscription'){
    this.cancelSubscription()
  }
}
}





