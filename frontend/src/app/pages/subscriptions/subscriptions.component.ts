import {Component, ElementRef, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto} from "../../dtos/user";
import {Observable} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {Location} from "@angular/common";

declare var paypal;


@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.scss']
})



export class SubscriptionsComponent implements OnInit {

  animal: AnimalDetailDto = {
    birthDate: null,
    createdOn: null,
    height: 0,
    id: "",
    name: "",
    race: "",
    shelter: undefined,
    species: undefined,
    weight: 0,
  };

  user: UserDetailDto = {
    paypalClientId: "",
    paypalSecret: "",
    email: "",
    id: "",
    role: ""
  };

  subscription: CreateSubscriptionDto = {
    subscriptionId: "",
    tier: "",
    user: undefined,
    animal: undefined,
  }

  typeForDeletion: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';
  protected subscriptionList: SubscriptionDetailDto[];


  constructor(
    public authService: AuthService,
    private userService: UserService,
    private notification: ToastrService,
    private location: Location,
    private translator: TranslateService) {
  }



  ngOnInit() {

    //loading the currently active user
    let userObservable: Observable<UserDetailDto> = this.userService.getCurrentUser()
    userObservable.subscribe({
      next: data => {
        this.user = data;
        this.subscription.user = this.user;
        console.log(this.user)

        //loading the subscriptions of the user
        let subscriptionObservable: Observable<SubscriptionDetailDto[]> = this.userService.findAllSubscriptionsByUser(this.user)
        subscriptionObservable.subscribe({
          next: data => {
            this.subscriptionList = data;
            console.log("subscription: " + this.subscriptionList);
          },
          error: error => {
            //this schould never happen except if no subscriptions have been made
            console.error('Error finding subscriptions', error);
            this.notification.error("Could not find subscriptions");
          }
        });


      },
      error: error => {
        //this schould never happen be
        console.error('Error finding user', error);
        this.notification.error("Could not find logged in user");
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  subscriptionForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = this.animal.name;
    this.typeForDeletion = "Subscription";
  }


  cancelSubscription(subscriptionId: string): void {
    if (subscriptionId) {
      // Use the PayPal SDK to cancel the subscription
      paypal.subscription.cancel(subscriptionId)
        .then((response) => {
          console.log('Subscription canceled successfully:', response);
          // Optionally, update your local state or notify the user
        })
        .catch((error) => {
          console.error('Error canceling subscription:', error);
          // Handle the error, e.g., notify the user
        });
    } else {
      console.warn('Subscription ID not available. Unable to cancel subscription.');
      // Handle the case where subscription ID is not available
    }
  }

  checkSubscription(subscriptionId: string): void {
    this.userService.checkSubscriptionStatus(subscriptionId).subscribe(
      (response) => {
        // Check the subscription status in the response
        console.log('Subscription Status:', response.status);
      },
      (error) => {
        console.error('Error checking subscription status:', error);
        // Handle the error
      }
    );
  }



}

