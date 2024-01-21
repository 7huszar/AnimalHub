import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {debounceTime, Observable, Subject} from 'rxjs';
import {UserDetailDto} from "../../../dtos/user";
import {UserService} from "../../../services/user.service";
import {NotifierService} from "../../../services/notifier.service";
import {ShelterDetailDto, ShelterListDto, ShelterSearch} from "../../../dtos/shelter";
import {ShelterService} from "../../../services/shelter.service";

@Component({
  selector: 'app-user-create-edit',
  templateUrl: './user-edit.component.html',
})
export class UserEditComponent implements OnInit {

  user: UserDetailDto = {
    email: "",
    role: "",
    id: ""
  };

  shelter: ShelterDetailDto = {
    address: "",
    createdOn: undefined,
    donationId: "",
    id: "",
    name: "",
    phoneNumber: "",
    subscriptionId: "",
    email: "",
  };


  searchChangedObservable = new Subject<void>();
  shelters: ShelterListDto[] = [];
  shelterSearchParams: ShelterSearch = {
    name: '',
  };

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: NotifierService,
    private shelterService: ShelterService,
  ) {
  }

  typeForDeletion: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';

  assignShelterName: string = '';
  assignShelterId: string = '';


  userForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
    this.typeForDeletion = "User";
  }

  public get submitButtonText(): string {
    return 'save changes';
  }


  private get modeActionFinished(): string {
    return 'edited';
  }

  ngOnInit(): void {
    this.reloadShelters();

    this.route.paramMap.subscribe((params: ParamMap) => {
      const id: string = String(params.get('id'));
      if (id != null) {
        this.userService.findById(String(id))
          .subscribe({
            next: data => {
              console.log(data)
              this.user = data;
              this.reloadAssignedTo()
            },
            error: error => {
              console.error('Error fetching User', error);
              const errorMessage = error.error ? error.error.message : 'Unknown error';
              this.notification.notifyError(errorMessage, "The requested User was not found");
            }
          })
      }
    });


    this.searchChangedObservable.pipe(debounceTime(300)).subscribe({
      next: () => {
        this.reloadShelters();
      },
    });

  }

  reloadShelters() {
    this.shelterService.search(this.shelterSearchParams).subscribe({
      next: (data) => {
        this.shelters = data;
      },
      error: (error) => {
        console.error('Error fetching Shelters', error);
        const errorMessage = error.status === 0 ? 'Is the backend up?' : error.message.message;
        this.notification.notifyError(errorMessage, 'Could Not Fetch Shelters');
      },
    });
  }


  reloadAssignedTo() {
    this.shelterService.isAssignedTo(this.user.id).subscribe({
      next: (data) => {
        this.shelter = data
        console.log(data)
      }, error: err => {
        console.error('Error checking if User is assigned to a Shelter:', err);
        const errorMessage = err.error ? err.error.message : 'Unknown error';
        this.notification.notifyError(errorMessage, "Error finding associated Shelter");
      }
    });
  }

  shelterSearchChanged(): void {
    this.searchChangedObservable.next();
  }


  public onSubmit(form: NgForm): void {
    console.log('is form valid?', form.valid, this.user);
    if (form.valid) {
      console.log(this.user.role)

      let observable: Observable<UserDetailDto>;
      observable = this.userService.update(this.user);

      observable.subscribe({
        next: data => {
          this.notification.notifySuccess(`User ${this.user.email} successfully ${this.modeActionFinished}.`);
          this.navigateSuccess();

        },
        error: error => {

          console.error(`Error editing User.` + error);
          const errorMessage = error.error ? error.error.message : 'Unknown error';
          this.notification.notifyError(errorMessage, "Error updating User Role");
        }
      });
    }
  }

  private navigateSuccess(): void {
    this.router.navigate(['/admin']);
  }

  public unassignShelter(id: string, name: string) {
    this.assignShelterId = id
    this.assignShelterName = name

    let observable: Observable<ShelterDetailDto>;
    observable = this.shelterService.unassignShelter(this.assignShelterId, this.user);
    observable.subscribe({
      next: data => {
        this.notification.notifySuccess(`Shelter ${this.assignShelterName} successfully assigned to user ${this.user.email}.`);
        this.reloadSite()
      },
      error: error => {

        console.error(`Error assigning Shelter to User.` + error);
        const errorMessage = error.error ? error.error.message : 'Unknown error';
        this.notification.notifyError(errorMessage, "Error assigning Shelter");
      }
    });

  }


  public assignShelter(id: string, name: string){
    this.assignShelterId = id
    this.assignShelterName= name

    console.log(this.shelter)
    console.log(this.user)

    let observable: Observable<ShelterDetailDto>;
    observable = this.shelterService.assignShelter(this.assignShelterId, this.user);

    observable.subscribe({
      next: data => {
        this.notification.notifySuccess(`Shelter ${this.assignShelterName} successfully assigned to user ${this.user.email}.`);
        this.reloadAssignedTo()
        this.reloadSite()
      },
      error: error => {

        console.error(`Error assigning Shelter to User.` + error);
        const errorMessage = error.error ? error.error.message : 'Unknown error';
        this.notification.notifyError(errorMessage, "Error assigning Shelter");
      }
    });
  }


  reloadSite() {
    this.router.navigateByUrl('/users/edit/'+ this.user.id, {skipLocationChange: true}).then(() => {
      window.location.reload();
    });
  }

}
