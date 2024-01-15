import {Component, ElementRef, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto} from "../../dtos/user";
import {Observable} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {Location} from "@angular/common";
import {PaypalService} from "../../services/paypal.service";



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
    gender: null,
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
  cancelShelter: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';
  protected subscriptionList: SubscriptionDetailDto[];


  constructor(
    public authService: AuthService,
    private userService: UserService,
    private paypalService: PaypalService,
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
        console.log("current User: " + this.user)


        //loading the subscriptions of the user
        let subscriptionObservable: Observable<SubscriptionDetailDto[]> = this.userService.findAllSubscriptionsByUser(this.user)
        subscriptionObservable.subscribe({
          next: data => {
            this.subscriptionList = data;
            console.log("Loaded subscriptions " + this.subscriptionList)
          },
          error: error => {
            //this schould never happen except if no subscriptions have been made
            console.error('Error finding subscriptions', error);
            this.notification.error("Could not find subscriptions");
          }
        });


      },
      error: error => {
        //this schould never happen
        console.error('Error finding user', error);
        this.notification.error("Could not find logged in user");
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  subscriptionForDeletion(name: string, id: string, shelterId: string ) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
    this.typeForDeletion = "Subscription";
    this.cancelShelter = shelterId;
  }



}

