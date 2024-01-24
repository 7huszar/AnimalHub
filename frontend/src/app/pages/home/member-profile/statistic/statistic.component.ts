import {Component, OnInit} from '@angular/core';
import {DonationListDto, ShelterDetailDto, ShelterDto} from "../../../../dtos/shelter";
import {ShelterService} from "../../../../services/shelter.service";
import {AuthService} from "../../../../services/auth.service";
import Decimal from "decimal.js";

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent implements OnInit{
  donations: DonationListDto[];
  total: Decimal = new Decimal(0);
  shelter: ShelterDto;

  constructor(
    private shelterService: ShelterService,
    protected authService: AuthService
  ) {
    this.shelter = this.authService.getUser().shelter;
  }

  ngOnInit() {
    this.shelterService.findAllDonations(this.shelter.id).subscribe({
      next: next => {
        this.donations = next;
        this.total = new Decimal(0);
        this.donations.forEach(donation => {
          this.total = this.total.add(donation.amount);
        });
        console.log(next);
      },
      error: error => {
        console.error(error);
      }
    });
  }

  formatMoney(donation: DonationListDto): string {
    return new Decimal(donation.amount).toFixed(2) + ' €';
  }

  mapType(type: string): string {
    if (type === 'ONE_TIME') {
      return 'one time donation';
    } else if (type === 'SUBSCRIPTION') {
      return 'subscription payment';
    }
    return 'unknown type';
  }
}
