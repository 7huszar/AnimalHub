import {Component, OnInit} from '@angular/core';
import {DonationListDto} from "../../../../dtos/shelter";
import {ShelterService} from "../../../../services/shelter.service";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent implements OnInit{
  donations: DonationListDto[];

  constructor(
    private shelterService: ShelterService,
    protected authService: AuthService
  ) {
  }

  ngOnInit() {
    this.shelterService.findAllDonations(this.authService.getUser().shelter.id).subscribe({
      next: next => {
        this.donations = next;
        console.log(next);
      },
      error: error => {
        console.error(error);
      }
    });
  }

}
