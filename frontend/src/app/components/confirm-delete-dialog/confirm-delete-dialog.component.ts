import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ShelterService} from "../../services/shelter.service";
import {UserService} from "../../services/user.service";
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../services/notifier.service";
import {Router} from "@angular/router";
import {AnimalService} from "../../services/animal.service";
import {Observable} from "rxjs";

declare var paypal;


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


//TODO This is not good ? window.location sollte in der einzelphase nicht benutzt werden bitte alternative finden
  reloadAdmin() {
    this.router.navigateByUrl('/admin', { skipLocationChange: true }).then(() => {
      window.location.reload();
    });
  }


  reloadAnimal() {
    this.router.navigate(['/shelter/' + this.DeleteId])
  }

  reloadSubscriptions() {
    this.router.navigate(['/subscriptions'])
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

//Used to check if subscription is active
  public cancelSubscription2() {
    //checking if subscription is still active
    let deleteSubscription: Observable<any> = this.userService.checkSubscriptionStatus(this.DeleteId)
    deleteSubscription.subscribe({
      next: data => {

        console.log(data)
      },
      error: error => {
        console.error('Error checking if subscription is active', error);
        this.notification.notifyError(error.error.message, error.error.error);
      }
    });

  }




  public cancelSubscription() {
  if (this.DeleteId != null) {

        //checking if subscription is still active
        let deleteSubscription: Observable<any> = this.userService.cancelSubscription(this.DeleteId, "kein bock mehr auf abo")
        deleteSubscription.subscribe({
          next: data => {
            this.notification.notifySuccess(`Subscription to ${this.deleteWhat} successfully canceled.`);
            this.reloadSubscriptions();
            },
          error: error => {
            console.error('Error canceling', error);
            this.notification.notifyError(error.error.message, error.error.error);
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





