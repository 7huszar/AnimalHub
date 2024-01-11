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
  private noBearerSoThatICanUSePaypalItTookWayToLongToFindThisError: HttpClient;

  //private BuyerId: string = "AQIxpNWdDXGB9l9l_d885Jxf5bLFQF4xG_5IRYEtF6nkQls6q4rSivsvH1hpgWdJWEo94XbHOkqyQqHD";
  //private Buyersecret: string = "EDEGnfRd7X1ccyWxK_vl-aKBgHgsWDKol0Ab_S30avCmMx5VDzGbwxU689225xiNx8nbnFts9IC1_VD8";



  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
    handler: HttpBackend) {
    this.noBearerSoThatICanUSePaypalItTookWayToLongToFindThisError = new HttpClient(handler);
  }


//Frontend Logic for canceling subscription works but no longer used because unsafe keep it just in case pls
  cancelSubscription(subscriptionId: string, cancelReason: string): Observable<any> {
    return this.checkSubscriptionStatus(subscriptionId).pipe(
      switchMap(({ accessToken, subscriptionStatus }) => {
        if (subscriptionStatus.status === 'ACTIVE') {
          const url = `${this.paypalApiUrl}${subscriptionId}/cancel`;
          console.log("subscriptionID " + subscriptionId);
          console.log("access token " + accessToken);

          const headers = new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`,
          });

          const body = { reason: cancelReason };

          return this.noBearerSoThatICanUSePaypalItTookWayToLongToFindThisError.post(url, body, { headers });
        } else {
          // Subscription is not active, handle the logic accordingly
          console.log("error couldn't cancel subscription");
        }
      })
    );
  }

  //DO NOT DELETE THIS NOT IMPLEMENTED IN THE BACKEND YET USE THIS IN FUTURE TO CHECK IF YOU SCHOULD SHOW THE CONTENT OR BLURR IT ILL HANDLE CANCELLING SUBSCRIPTIONS
  checkSubscriptionStatus(subscriptionId: string): Observable<any> {
    return this.getPayPalAccessToken().pipe(
      switchMap((accessTokenResponse) => {
        const accessToken = accessTokenResponse.access_token;
        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`,
        });
        console.log("access Token " + accessToken);
        const url = `${this.paypalApiUrl}${subscriptionId}`;

        return this.noBearerSoThatICanUSePaypalItTookWayToLongToFindThisError.get<any>(url, { headers }).pipe(
          map(subscriptionStatus => ({ accessToken, subscriptionStatus }))
        );
      })
    );
  }

  //Implemented in backend but still dont delete just in case
  getPayPalAccessToken(): Observable<any> {


    const clientId = this.SellerId;
    const secret = this.Sellersecret;

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + btoa(`${clientId}:${secret}`)
    });

    const body = new HttpParams().set('grant_type', 'client_credentials');
    return this.noBearerSoThatICanUSePaypalItTookWayToLongToFindThisError.post(this.paypalTokenUrl, body,{headers});
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


}
