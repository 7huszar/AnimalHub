import {Injectable} from '@angular/core';
import {HttpBackend, HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {SubscriptionDetailDto} from "../dtos/user";
import {ShelterDetailDto} from "../dtos/shelter";
import {PaypalDonationDto, PaypalLinkDto} from "../dtos/paypal";


@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  private usersBaseUri: string = this.globals.backendUri + '/users';

  private SellerId: string = "AfH_enM1Jb3sXHqaZ5V2LpTN-xJbPXQLmFogThTBDLzMvw-81iGtaX1wnIj_NwJhIUtRnQqUypiRdfxv";
  private Sellersecret: string = "EB2gtcxcvRY6-46X9G1XiQ4hZXN9FTC8moRaStTRUme7z65v-6rj7Vt3nA-V3aSHnQ9IWpwc8nNYxoZO";




  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
    handler: HttpBackend) {
  }

  /**
   * Cancel an existing subscription and delete it from the system.
   *
   * @param id of the subscription to be canceled and deleted
   * @param shelterId
   */
  cancelSubscriptions(id: string, shelterId: string): Observable<any> {
    const body = { shelterId: shelterId }; // Create an object with the shelterId property
    return this.httpClient.post<SubscriptionDetailDto>(`${this.usersBaseUri}/paypal/${id}`, { body });
  }


  checkIfSubscriptionIsActive(id: string, shelterDto: ShelterDetailDto): Observable<any> {
    // Convert the shelterDto to query parameters
    const params = new HttpParams()
      .set('param', shelterDto.id)

    return this.httpClient.get<SubscriptionDetailDto>(`${this.usersBaseUri}/paypal/${id}`, { params });
  }

  isSubscribed(animalId: string): Observable<SubscriptionDetailDto> {
    return this.httpClient.get<SubscriptionDetailDto>(`${this.usersBaseUri}/subscriptions/${animalId}`);
  }

  createSubscription(id: string): Observable<any> {
    return this.httpClient.post<string>(`${this.usersBaseUri}/subscriptions/${id}`, {});
  }

  placeOrder(receiver: string, donationDto: PaypalDonationDto): Observable<PaypalLinkDto> {
    return this.httpClient.post<PaypalLinkDto>(this.globals.backendUri + '/paypal/donate/' + receiver, donationDto);
  }

  captureOrder(id: string): Observable<any> {
    return this.httpClient.post<any>(this.globals.backendUri + '/paypal/capture/' + id, {});
  }
}
