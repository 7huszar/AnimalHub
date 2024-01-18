import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {ShelterService} from "../../services/shelter.service";
import {UserService} from "../../services/user.service";
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../services/notifier.service";
import {Router} from "@angular/router";
import {AnimalService} from "../../services/animal.service";
import {Observable} from "rxjs";
import {PaypalService} from "../../services/paypal.service";
import {Mode} from "../../enums/modes";


@Component({
  selector: 'app-confirm-delete-dialog',
  templateUrl: './confirm-delete-dialog.component.html',
  styleUrls: ['./confirm-delete-dialog.component.scss'],
})

export class ConfirmDeleteDialogComponent {

  @Output() deleteErrorEvent = new EventEmitter<{ error: string | null, errorMessage: string }>();

  @Input() deleteName: string | undefined = '';
  @Input() deleteMode: Mode;
  @Input() DeleteId: string | undefined = '';
  @Input() cancelShelter: string | undefined;

  deleteModeString: string;


  @HostBinding('class') cssClass = 'modal-fade';

  constructor(
    private shelterService: ShelterService,
    private animalService: AnimalService,
    private paypalService: PaypalService,
    private notification: NotifierService,
    private userService: UserService,
    private router: Router,
  ) {
  }

  ngOnInit(){
    this.deleteModeString = this.deleteMode === 3? "USER" : this.deleteMode === 6 ? "ANIMAL" : this.deleteMode === 9 ? "SHELTER" : "undefined";
  }

  public deleteShelter() {
    if (this.DeleteId != null) {
      console.log(this.DeleteId)

      this.shelterService.delete(this.DeleteId)
        .subscribe({
          next: () => {
            this.notification.notifySuccess(`Animal ${this.deleteName} successfully deleted.`);
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
    this.router.navigateByUrl('/admin', {skipLocationChange: true}).then(() => {
      window.location.reload();
    });
  }


  reloadAnimal() {
    this.router.navigate(['/shelter/' + this.DeleteId])
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
            this.notification.notifySuccess(`Shelter ${this.deleteName} successfully deleted.`);
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
  public checkSubscription() {

    let test: Observable<void> = this.paypalService.checkIfSubscriptionIsActive(this.DeleteId, null)
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

//TODO implement this somewhere when a shelter is being created

  // public createSubscriptionPlan() {
  //
  //   if (this.DeleteId != null) {
  //     console.log(this.cancelShelter)
  //     let test: Observable<void> = this.paypalService.createSubscription(this.cancelShelter)
  //     test.subscribe({
  //       next: data => {
  //         console.log(data)
  //         this.notification.notifySuccess(`SubscriptionPlan to successfully created.`);
  //         this.reloadSubscriptions();
  //       },
  //       error: error => {
  //         //this schould never happen except if no subscriptions have been made
  //         console.error('Error creating Plan', error);
  //       }
  //     });
  //   }
  // }



public deleteEntity() {
  if (this.deleteMode === Mode.USER) {
    this.deleteUser()
  } else if(this.deleteMode === Mode.SHELTER) {
    this.deleteShelter()
  }else if (this.deleteMode === Mode.ANIMAL){
    this.deleteAnimal()
  }
  // else if (this.deleteMode === 'Subscription'){
  //   this.createSubscriptionPlan()
  // }
}
}





