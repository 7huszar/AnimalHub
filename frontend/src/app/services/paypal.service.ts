import {Injectable} from '@angular/core';
import {HttpBackend, HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {map, Observable, Subscription, switchMap, throwError} from 'rxjs';
import {Globals} from '../global/globals';
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto, UserSearch} from "../dtos/user";
import {ShelterDetailDto} from "../dtos/shelter";


@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  private usersBaseUri: string = this.globals.backendUri + '/users';
  private paypalApiUrl = 'https://api.sandbox.paypal.com/v1/billing/subscriptions/';
  private paypalTokenUrl = 'https://api.sandbox.paypal.com/v1/oauth2/token';

  private SellerId: string = "AfH_enM1Jb3sXHqaZ5V2LpTN-xJbPXQLmFogThTBDLzMvw-81iGtaX1wnIj_NwJhIUtRnQqUypiRdfxv";
  private Sellersecret: string = "EB2gtcxcvRY6-46X9G1XiQ4hZXN9FTC8moRaStTRUme7z65v-6rj7Vt3nA-V3aSHnQ9IWpwc8nNYxoZO";

  //private BuyerId: string = "AQIxpNWdDXGB9l9l_d885Jxf5bLFQF4xG_5IRYEtF6nkQls6q4rSivsvH1hpgWdJWEo94XbHOkqyQqHD";
  //private Buyersecret: string = "EDEGnfRd7X1ccyWxK_vl-aKBgHgsWDKol0Ab_S30avCmMx5VDzGbwxU689225xiNx8nbnFts9IC1_VD8";



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



}
