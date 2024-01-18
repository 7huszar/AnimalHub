import {Component, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Observable} from 'rxjs';
import {UserDetailDto} from "../../../dtos/user";
import {UserService} from "../../../services/user.service";

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

  constructor(
    private service: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
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
              this.notification.error('The requested User was not found')
            }
          })
      }
    });
  }


  public onSubmit(form: NgForm): void {
    console.log('is form valid?', form.valid, this.user);
    if (form.valid) {
      console.log(this.user.role)

      let observable: Observable<UserDetailDto>;
      observable = this.service.update(this.user);

      observable.subscribe({
        next: data => {
          this.notification.success(`User ${this.user.email} successfully ${this.modeActionFinished}.`);
          this.router.navigate(['/admin']);
        },
        error: error => {

          console.error(`Error editing User.`);
          this.notification.error(error.error.message, error.error.error);
        }
      });
    }
  }
}
