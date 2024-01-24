import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";
import {DonationSumDto} from "../dtos/shelter";

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
    return this.http.get<DonationSumDto[]>(this.shelterBaseUri + '/' + shelterId);
  }

}
