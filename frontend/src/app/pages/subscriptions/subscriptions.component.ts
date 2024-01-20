import {Component, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {SubscriptionDetailDto, UserDetailDto} from "../../dtos/user";
import {Observable} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {TranslateService} from "@ngx-translate/core";
import {Location} from "@angular/common";
import {AnimalService} from "../../services/animal.service";
import {NotifierService} from "../../services/notifier.service";


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
    englishRace: null,
    germanRace: null,
    shelter: undefined,
    species: undefined,
    weight: 0,
  };

  user: UserDetailDto = {
    email: "",
    id: "",
    role: ""
  };

  subscription: SubscriptionDetailDto = {
    cancelled: false,
    id: "",
    subscriptionId: "",
    tier: "",
    user: undefined,
    animal: undefined,
    profilePicture: undefined,
  }

  typeForDeletion: string = '';
  cancelShelter: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';
  protected subscriptionList: SubscriptionDetailDto[];
  protected shelterImageURL: any = null;
  protected imageUrls: string[] = [];


  constructor(
    public authService: AuthService,
    private userService: UserService,
    private animalService: AnimalService,
    private notification: NotifierService,
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
            console.log(data)
            this.subscriptionList = data;
            console.log("Loaded subscriptions " + this.subscriptionList)

            this.subscriptionList.forEach((subscription: SubscriptionDetailDto) => {
              this.animalService.getProfileImage(subscription.animal.id).subscribe({
                next: blob => {
                  subscription.profilePicture = URL.createObjectURL(blob);
                }, error: err => {
                  console.error(err);
                  const errorMessage = err.error ? err.error.message : 'Unknown error';
                  this.notification.notifyError(errorMessage, "Could not load Picture of shelter");
                }
              });


            });
          },
          error: error => {
            //this schould never happen except if no subscriptions have been made
            console.error('Error finding subscriptions', error);
            const errorMessage = error.error ? error.error.message : 'Unknown error';
            this.notification.notifyError(errorMessage, "Could not find subscriptions");
          }
        });


      },
      error: error => {
        //this schould never happen
        console.error('Error finding user', error);
        const errorMessage = error.error ? error.error.message : 'Unknown error';
        this.notification.notifyError(errorMessage, "Could not find logged in user");
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  subscriptionForDeletion(name: string, id: string, shelterId: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
    this.typeForDeletion = "Subscription";
    this.cancelShelter = shelterId;
  }


}

