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

declare var paypal;


@Component({
  selector: 'app-animal-homepage',
  templateUrl: './animal-homepage.component.html',
  styleUrls: ['./animal-homepage.component.scss']
})
export class AnimalHomepageComponent implements OnInit {

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

  // TODO: add fitting animal attributes
  protected animalAge: number;
  protected contents: FileDto[];
  protected newContent: File = null;

  constructor(
    protected authService: AuthService,
    private service: AnimalService,
    private userService: UserService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private el: ElementRef,

  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if (params.get("id")) {
        this.animal.id = params.get("id");
        let animalObservable: Observable<AnimalDetailDto> = this.service.findById(this.animal.id)
        animalObservable.subscribe({
          next: data => {
            this.animal = data;
            console.log(this.animal.shelter.subscriptionId)
            this.calculateAge(this.animal.birthDate)
          },
          error: error => {
            console.error('Error finding animal', error);
            this.notification.error("Could Not find animal");
          }
        });
      }
    });
    this.service.getContents(this.animal.id).subscribe({
      next: data => {
        this.contents = data;
      },
      error: error => {
        console.error('Error finding contents', error);
        this.notification.error("Could Not find contents of animal");
      }
    })


    // Load the PayPal script dynamically
    const script = document.createElement('script');
    //getting user
    let userObservable: Observable<UserDetailDto> = this.userService.getCurrentUser()
    userObservable.subscribe({
      next: data => {
        this.user = data;
        this.subscription.user=this.user;
        console.log(this.user)
      },
      error: error => {
        //this schould never happen
        console.error('Error finding user', error);
        this.notification.error("Could Not find logged in user to save");
      }
    });

    //TODO handle the case in which the user is already subscribed to the animal


    //TODO first of all dont touch it or u lose money
    //TODO change it for live version this is sandbox
    //TODO LOGIN details for transaction are test.t23est@test.com and 2023ws-se-pr-groupA
    script.src = 'https://www.paypal.com/sdk/js?client-id=ATDSVmhK53uv4p0BsnfQeNdgOUF77jr8u2TpuOnWqJ8CoNeGPrBmRo4DUFY1JpMEYiVYk5oLqu_oktoC&vault=true&intent=subscription';
    script.onload = () => {
      // PayPal script is loaded, render the button
      this.renderPayPalButton();
    };
    document.head.appendChild(script);
  }

  private renderPayPalButton(): void {
    // Check if the `paypal` object is defined
    if (typeof paypal === 'undefined') {
      console.error('PayPal SDK not loaded');
      return;
    }

    // Render the PayPal button
    paypal.Buttons({
      createSubscription: (data, actions) => {
        return actions.subscription.create({
          plan_id: this.animal.shelter.subscriptionId,
        });
      },
      onApprove: (data, actions) => {
        console.log(data)
        this.subscription.animal=this.animal;
        this.subscription.subscriptionId=data.subscriptionID;
        this.subscription.tier="1";
        console.log('Subscription Payload:', this.subscription);

        this.userService.createSubscription(this.subscription).subscribe(
          response => {
            // Handle the response
            console.log("Subscription created successfully:", response);
          },
          error => {
            // Handle the error
            console.error("Error creating subscription:", error);
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

  public onUpload(): void {
    const createFileDto: CreateFileDto = {file: this.newContent, name: this.newContent.name, summary: ""};
    this.service.addContent(this.animal.id, createFileDto).subscribe({
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

    let age = today.getFullYear() - birthdateDate.getFullYear();

    // Check if the birthday has occurred this year
    const todayMonth = today.getMonth();
    const birthdateMonth = birthdateDate.getMonth();

    if (todayMonth < birthdateMonth || (todayMonth === birthdateMonth && today.getDate() < birthdateDate.getDate())) {
      age--;
    }
    this.animalAge = age;
  }

  protected canUpload(): boolean {
    return this.authService.isAdmin() || (this.animal.shelter !== null && this.authService.isMemberOf(this.animal.shelter.id));
  }

  protected readonly Mode = Mode;
}
