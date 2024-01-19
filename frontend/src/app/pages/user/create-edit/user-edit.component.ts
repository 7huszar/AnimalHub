import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {debounceTime, Observable, Subject} from 'rxjs';
import {UserDetailDto} from "../../../dtos/user";
import {UserService} from "../../../services/user.service";
import {NotifierService} from "../../../services/notifier.service";
import {ShelterListDto, ShelterSearch} from "../../../dtos/shelter";
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

  searchChangedObservable = new Subject<void>();
  shelters: ShelterListDto[] = [];
  shelterSearchParams: ShelterSearch = {
    name: '',
  };

  constructor(
    private service: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private notification :NotifierService,
    private shelterService: ShelterService,
  ) {
  }

  typeForDeletion: string = '';
  idForDeletion: string = '';
  nameForDeletion: string = '';

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
        this.service.findById(String(id))
          .subscribe({
            next: data => {
              this.user = data;

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
        const errorMessage =
          error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
        this.notification.notifyError(
          errorMessage,
          'Could Not Fetch Shelters'
        );
      },
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
      observable = this.service.update(this.user);

      observable.subscribe({
        next: data => {
          this.notification.notifySuccess(`User ${this.user.email} successfully ${this.modeActionFinished}.`);
          this.router.navigate(['/admin']);
        },
        error: error => {

          console.error(`Error editing User.` + error);
          const errorMessage = error.error ? error.error.message : 'Unknown error';
          this.notification.notifyError(errorMessage, "Error updating User Role");
        }
      });
    }
  }

  public assignShelter(){

  }

}
