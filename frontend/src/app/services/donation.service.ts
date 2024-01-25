import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";
import {DonationPageDto, DonationSearchDto, DonationSumDto} from "../dtos/shelter";

@Injectable({
  providedIn: 'root'
})
export class DonationService {

  private shelterBaseUri: string = this.globals.backendUri + '/donation';

  constructor(
    private http: HttpClient,
    private globals: Globals) {
  }

  getDonationByReceiver(shelterId: string): Observable<DonationSumDto[]> {
    return this.http.get<DonationSumDto[]>(this.shelterBaseUri + '/sum/' + shelterId);
  }

  searchDonations(shelterId: string, searchDto: DonationSearchDto): Observable<DonationPageDto> {
    let params = new HttpParams();

    if (searchDto.dateAfter != null) {
      params = params.append('dateAfter', searchDto.dateAfter);
    }

    if (searchDto.dateBefore != null) {
      params = params.append('dateBefore', searchDto.dateBefore);
    }

    if (searchDto.amountAfter != null) {
      params = params.append('amountAfter', searchDto.amountAfter);
    }

    if (searchDto.amountBefore != null) {
      params = params.append('amountBefore', searchDto.amountBefore);
    }

    if (searchDto.id != null) {
      params = params.append('id', searchDto.id);
    }

    if (searchDto.page != null) {
      params = params.append('page', searchDto.page);
    }

    if (searchDto.pageSize != null) {
      params = params.append('pageSize', searchDto.pageSize);
    }

    if (searchDto.receiver != null) {
      params = params.append('receiver', searchDto.receiver);
    }

    return this.http.get<DonationPageDto>(this.shelterBaseUri + '/search/' + shelterId, {params});
  }
}
