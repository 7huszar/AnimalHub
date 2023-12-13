import {Component, OnInit} from '@angular/core';
import {debounceTime, Subject} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../../../services/user.service";
import {UserDetailDto, UserSearch} from "../../../dtos/user";

@Component({
  selector: 'app-user-admin-page',
  templateUrl: './user-admin-page.component.html',
  styleUrls: ['./user-admin-page.component.scss']
})
export class UserAdminPageComponent implements OnInit {

  idForDeletion: string = "";
  nameForDeletion: string = "";
  users: UserDetailDto[] = [];

  searchChangedObservable = new Subject<void>();
  searchParams: UserSearch = {
    email: '',
  };
  bannerError: string | null = null;


  constructor(
    private notification: ToastrService,
    private userService: UserService,
  ) {
  }


  ngOnInit(): void {
    this.reloadUsers();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reloadUsers()});
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  reloadUsers() {
    this.userService.search(this.searchParams)
      .subscribe({
        next: data => {
          this.users = data;
        },
        error: error => {
          console.error('Error fetching Users', error);
          this.bannerError = 'Could not fetch Users: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Users');
        }
      });
  }

  userForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
  }
}
