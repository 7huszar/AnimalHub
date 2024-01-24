import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto} from "../../../dtos/user";
import {UserService} from "../../../services/user.service";
import {NotifierService} from "../../../services/notifier.service";
import {AnimalDetailDto} from "../../../dtos/animal";
import {Observable} from "rxjs";
import {PaypalService} from "../../../services/paypal.service";
import {Router} from "@angular/router";

declare var paypal;

@Component({
  selector: 'app-subscribe',
  templateUrl: './subscribe.component.html',
  styleUrls: ['./subscribe.component.scss']
})

export class SubscribeComponent implements OnInit{

  @Input() animal: AnimalDetailDto = null;

  clicked: boolean = false;
  protected isSubscribed: boolean;

  subscription: CreateSubscriptionDto = {
    subscriptionId: "",
    tier: "",
    user: undefined,
    animal: undefined,
  };

  user: UserDetailDto = {
    email: "",
    id: "",
    role: ""
  };

  constructor(
    private userService: UserService,
    private notification: NotifierService,
    private el: ElementRef,
    private paypalService: PaypalService,
    private router: Router
  ) {
  }

  ngOnInit() {
    let isSubscribedObservable: Observable<SubscriptionDetailDto> = this.paypalService.isSubscribed(this.animal.id)
    isSubscribedObservable.subscribe({
      next: data => {
        console.log("User is subscribed to Animal")
        if (data != null) {
          //this is the case that the user is already subscribed
          this.isSubscribed = true;
          //TODO blurr animal content in this case
        } else {
          //if user is not subscribed load the paypal button

          // Load the PayPal script
          const script = document.createElement('script');
          //getting user
          let userObservable: Observable<UserDetailDto> = this.userService.getCurrentUser()
          userObservable.subscribe({
            next: data => {
              this.user = data;
              this.subscription.user = this.user;
            },
            error: error => {
              //this schould never happen
              console.error('Error finding user', error);
              this.notification.notifyError('error', "Could Not find logged in user to save");
            }
          });

          //TODO Receiving account animalhub.animalService@gmail.com and 2023ws-se-pr-groupA
          //TODO Business account for sandbox testing buisness.payer@gmail.com and 2023ws-se-pr-groupA

          script.src = 'https://www.paypal.com/sdk/js?client-id='+ this.animal.shelter.paypalClientId + '&vault=true&intent=subscription';
          script.onload = () => {
            // PayPal script is loaded, render the button
            this.renderPayPalButton();
          };
          document.head.appendChild(script);

        }
      },

      error: error => {
        console.error('Subscription could not be loaded', error);
        this.notification.notifyError('error', "Could Not verify subscription Status");
      }
    });

  }

  private renderPayPalButton(): void {
    // Check if the `paypal` object is defined
    if (typeof paypal === 'undefined') {
      console.error('PayPal SDK not loaded');
      return;
    }

    // Render the PayPal button
    //this loads the button and the subscription plan when the button is clicked
    paypal.Buttons({
      createSubscription: (data, actions) => {
        return actions.subscription.create({
          plan_id: this.animal.shelter.subscriptionId,
        });
      },
      onApprove: (data, actions) => {
        console.log(data)
        this.subscription.animal = this.animal;
        this.subscription.subscriptionId = data.subscriptionID;
        this.subscription.tier = "1";
        console.log('Subscription Payload:', this.subscription);
        console.log(data)
        this.userService.createSubscription(this.subscription).subscribe(
          response => {
            // Handle the response
            console.log("Subscription created successfully:", response);
            this.notification.notifySuccess("Successfully created subscription")
            this.reloadSubscriptions()
          },
          error => {
            // Handle the error
            console.error(error);
            const errorMessage = error.error ? error.error.message : 'Unknown error';
            this.notification.notifyError(errorMessage, "Could not load Picture of shelter");
          }
        );
      },
    }).render(this.el.nativeElement.querySelector('#paypal-button-container-P-2J073097VE083035WMWKWHZY'));
  }

  reloadSubscriptions() {
    this.router.navigateByUrl('/animal/'+ this.animal.id, {skipLocationChange: true}).then(() => {
      window.location.reload();
    });
  }

  click(): void {
    this.clicked = true;
  }
}
