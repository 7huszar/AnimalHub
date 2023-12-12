import {Component, OnInit} from '@angular/core';
import {debounceTime, Subject} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Shelter, ShelterListDto, ShelterSearch} from "../../dtos/shelter";
import {ShelterService} from "../../services/shelter.service";


export enum ShelterCreateEditMode {
  create,
  edit,
}

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss']
})
export class AdminPageComponent implements OnInit{

  mode: ShelterCreateEditMode = ShelterCreateEditMode.create;
  shelter: Shelter = {
    address: "",
    email: "",
    phoneNumber: "",
    name: ''
  };

  idForDeletion: string = "";
  nameForDeletion: string = "";
  shelters: ShelterListDto[] = [];
  searchChangedObservable = new Subject<void>();
  searchParams: ShelterSearch = {
    name: ''
  };
  searchName: string | null = null;
  bannerError: string | null = null;
  private service: ShelterService;


  constructor(
    private notification: ToastrService,
    private shelterService: ShelterService  // Inject ShelterService

  ) {
    this.service = shelterService;  // Assign it to the private variable

  }


  ngOnInit(): void {
    this.reloadShelters();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reloadShelters()});
  }


  searchChanged(): void {
    console.log("what" + this.searchParams.name);
    this.searchChangedObservable.next();
  }

  reloadShelters() {
    if (this.searchName) {
      this.searchParams.name = this.searchName;
    }

    this.service.search(this.searchParams)
      .subscribe({
        next: data => {
          this.shelters = data;
        },
        error: error => {
          console.error('Error fetching Shelters', error);
          this.bannerError = 'Could not fetch Shelters: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Shelters');
        }
      });
  }

  shelterForDeletion(name: string, id: string) {
    this.idForDeletion = id;
    this.nameForDeletion = name;
  }
}
