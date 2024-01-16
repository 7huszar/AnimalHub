import {Component, OnInit} from '@angular/core';
import {debounceTime, Subject} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {ShelterService} from '../../../services/shelter.service';
import {UserService} from '../../../services/user.service';
import {ShelterListDto, ShelterSearch} from '../../../dtos/shelter';
import {UserDetailDto, UserSearch} from '../../../dtos/user';
import {Mode} from "../../../enums/modes";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss'],
})
export class AdminPageComponent implements OnInit {

  modeForDeletion: Mode;
  idForDeletion: string = '';
  nameForDeletion: string = '';


  // Shelter-related properties
  shelters: ShelterListDto[] = [];
  shelterSearchParams: ShelterSearch = {
    name: '',
  };

  // User-related properties
  users: UserDetailDto[] = [];
  userSearchParams: UserSearch = {
    email: '',
    role: '',
  };

  // Common properties
  searchChangedObservable = new Subject<void>();
  bannerError: string | null = null;

  constructor(
    private notification: ToastrService,
    private shelterService: ShelterService,
    private userService: UserService
  ) {}


  ngOnInit(): void {
    this.reloadShelters();
    this.reloadUsers();
    this.searchChangedObservable.pipe(debounceTime(300)).subscribe({
      next: () => {
        this.reloadShelters();
        this.reloadUsers();
      },
    });
  }

  // Shelter-related methods
  shelterSearchChanged(): void {
    this.searchChangedObservable.next();
  }

  reloadShelters() {
    this.shelterService.search(this.shelterSearchParams).subscribe({
      next: (data) => {
        this.shelters = data;
      },
      error: (error) => {
        console.error('Error fetching Shelters', error);
        this.bannerError = 'Could not fetch Shelters: ' + error.message;
        const errorMessage =
          error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
        this.notification.error(
          errorMessage,
          'Could Not Fetch Shelters'
        );
      },
    });
  }

  shelterForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
    this.typeForDeletion = "Shelter";
  }

  // User-related methods
  userSearchChanged(): void {
    this.searchChangedObservable.next();
  }

  reloadUsers() {
    this.userService.search(this.userSearchParams)
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
    this.typeForDeletion = "User";
  }

  // Common methods
  onDeleteError(event: { error: string | null; errorMessage: string }) {
    if (event.error !== null) {
      this.notification.error(
        `Error: ${event.errorMessage}`,
        'Deletion Error'
      );
    }
  }
}
