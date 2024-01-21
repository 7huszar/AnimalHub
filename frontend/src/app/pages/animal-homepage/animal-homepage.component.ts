import {Component, ElementRef, OnInit} from '@angular/core';
import {AnimalDetailDto} from "../../dtos/animal";
import {AnimalService} from "../../services/animal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Mode} from "../../enums/modes";
import {CreateFileDto, FileDto} from "../../dtos/file";
import {AuthService} from "../../services/auth.service";
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto} from "../../dtos/user";
import {UserService} from "../../services/user.service";
import {Species} from "../../dtos/species";
import {PaypalService} from "../../services/paypal.service";
import {Gender} from "../../dtos/gender";
import {Globals} from "../../global/globals";
import {NotifierService} from "../../services/notifier.service";

declare var paypal;


@Component({
  selector: 'app-animal-homepage',
  templateUrl: './animal-homepage.component.html',
  styleUrls: ['./animal-homepage.component.scss']
})
export class AnimalHomepageComponent implements OnInit {

  animal: AnimalDetailDto = {
    birthDate: new Date(),
    height: 0,
    id: "",
    name: "",
    gender: null,
    englishRace: "",
    germanRace: "",
    shelter: undefined,
    species: undefined,
    weight: 0,
  };

  user: UserDetailDto = {
    email: "",
    id: "",
    role: ""
  };

  subscription: CreateSubscriptionDto = {
    subscriptionId: "",
    tier: "",
    user: undefined,
    animal: undefined,
  };

  protected animalAge: number;
  protected contents: FileDto[];
  protected newContent: File = null;

  protected years: boolean;
  protected months: boolean;
  protected days: boolean;
  protected year: boolean;
  protected month: boolean;
  protected day: boolean;

  protected isDog: boolean;
  protected isCat: boolean;
  protected isBird: boolean;
  protected isRodent: boolean;
  protected isSubscribed: boolean;


  constructor(
    protected globals: Globals,
    protected authService: AuthService,
    private animalService: AnimalService,
    private paypalService: PaypalService,
    private userService: UserService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private betternotifier: NotifierService,
    private el: ElementRef,
    private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if (params.get("id")) {
        this.animal.id = params.get("id");
        let animalObservable: Observable<AnimalDetailDto> = this.animalService.findById(this.animal.id)
        animalObservable.subscribe({
          next: data => {
            this.animal = data;
            console.log(this.animal.shelter.subscriptionId)
            this.calculateAge(this.animal.birthDate)

            switch (this.animal.species) {
              case this.animal.species = Species.cat: {
                this.isCat = true;
                break;
              }
              case this.animal.species = Species.dog: {
                this.isDog = true;
                break;
              }
              case this.animal.species = Species.bird: {
                this.isBird = true;
                break;
              }
              case this.animal.species = Species.rodent: {
                this.isRodent = true;
                break;
              }
            }

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
                      this.notification.error("Could Not find logged in user to save");
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
                this.notification.error("Could Not verify subscription Status");
              }
            });


          },
          error: error => {
            console.error('Error finding animal', error);
            this.notification.error("Could Not find animal");
          }
        });
      }
    });
    this.animalService.getContents(this.animal.id).subscribe({
      next: data => {
        this.contents = data;
      },
      error: error => {
        console.error('Error finding contents', error);
        this.notification.error("Could Not find contents of animal");
      }
    })


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
            this.betternotifier.notifySuccess("Successfully created subscription")
            this.reloadSubscriptions()
          },
          error => {
            // Handle the error
            console.error(error);
            const errorMessage = error.error ? error.error.message : 'Unknown error';
            this.betternotifier.notifyError(errorMessage, "Could not load Picture of shelter");
          }
        );
      },
    }).render(this.el.nativeElement.querySelector('#paypal-button-container-P-2J073097VE083035WMWKWHZY'));
  }

  public onNewContentSelected(event: any): void {
    if (event.target.files[0]) {
      this.newContent = event.target.files[0];
    }
  }


  reloadSubscriptions() {
    this.router.navigateByUrl('/animal/'+ this.animal.id, {skipLocationChange: true}).then(() => {
      window.location.reload();
    });
  }

  public onUpload(): void {
    const createFileDto: CreateFileDto = {
      file: this.newContent,
      name: this.newContent.name,
      summary: "TODO make summary"
    };
    this.animalService.addContent(this.animal.id, createFileDto).subscribe({
      next: fileDto => {
        console.log(fileDto);
        this.newContent = null;
        this.contents.unshift(fileDto);
      },
      error: err => {
        console.error(err); // TODO handle
      }
    });
  }

  public calculateAge(birthdate: Date): void {
    const today = new Date();
    const birthdateDate = new Date(birthdate);

    // Convert the milliseconds to years
    let timeDifference = (today.getTime() - birthdateDate.getTime()) / (24 * 60 * 60 * 1000);
    let age = timeDifference;
    console.log("AGE " + timeDifference);
    if (timeDifference > 365.25) {
      age = Math.floor(timeDifference / 365.25);
      if (age > 1) {
        this.years = true;
      } else {
        this.year = true;
      }
    }
    if (timeDifference < 365.25 && timeDifference > 30) {
      age = Math.floor(timeDifference / (30));
      if (age > 1) {
        this.months = true;
      } else {
        this.month = true;
      }
    }
    if (30 > timeDifference) {
      age = Math.floor(timeDifference);
      if (age > 1) {
        this.days = true;
      } else {
        this.day = true;
      }
    }
    this.animalAge = age;
  }

  protected canUpload(): boolean {
    return this.authService.isAdmin() || (this.animal.shelter && this.authService.isMemberOf(this.animal.shelter.id));
  }

  protected readonly Mode = Mode;
  protected readonly Gender = Gender;
}
