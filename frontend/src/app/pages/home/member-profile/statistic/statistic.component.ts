import {Component, OnInit} from '@angular/core';
import {DonationListDto, DonationSearchDto, ShelterDto} from "../../../../dtos/shelter";
import {AuthService} from "../../../../services/auth.service";
import Decimal from "decimal.js";
import {DonationService} from "../../../../services/donation.service";
import {debounceTime, Subject} from "rxjs";

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})
export class StatisticComponent implements OnInit {
  donations: DonationListDto[];
  total: Decimal = new Decimal(0);
  shelter: ShelterDto;
  searchChangedObservable = new Subject<void>();
  page: number = 0;
  maxPage: number = 0;

  searchParams: DonationSearchDto = {
    dateAfter: null,
    dateBefore: null,
    amountAfter: null,
    amountBefore: null,
    id: '',
    page: 0,
    pageSize: 10};

  constructor(
    protected authService: AuthService,
    private donationService: DonationService,
  ) {
    this.shelter = this.authService.getUser().shelter;
  }

  ngOnInit() {
    this.reloadDonations();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reloadDonations()});
  }

  formatMoney(donation: DonationListDto): string {
    return new Decimal(donation.amount).toFixed(2) + ' €';
  }

  reloadDonations(): void {
    this.donationService.searchDonations(this.shelter.id, this.searchParams).subscribe({
      next: next => {
        this.donations = next.donations;
        this.page = next.currentPage;
        this.maxPage = next.maxPage;

        this.total = new Decimal(0);
        this.donations.forEach(donation => {
          this.total = this.total.add(donation.amount);
        });
      },
      error: error => {
        console.error(error);
      }
    });
  }

  nextPage(): void {
    this.page++;
    this.searchParams.page = this.page;
    this.reloadDonations();
  }

  prevPage(): void {
    this.page--;
    this.searchParams.page = this.page;
    this.reloadDonations();
  }

  searchChanged() :void {
    this.searchChangedObservable.next();
  }
}
