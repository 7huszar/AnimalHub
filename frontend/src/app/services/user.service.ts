import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, Subscription, switchMap} from 'rxjs';
import {Globals} from '../global/globals';
import {CreateSubscriptionDto, SubscriptionDetailDto, UserDetailDto, UserSearch} from "../dtos/user";
import {AnimalDetailDto, CreateAnimalDto} from "../dtos/animal";
import {ShelterDto} from "../dtos/shelter";
import {SubscriptionLog} from "rxjs/internal/testing/SubscriptionLog";



@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersBaseUri: string = this.globals.backendUri + '/users';
  private paypalApiUrl = 'https://api.sandbox.paypal.com/v1/billing/subscriptions/';
  private paypalTokenUrl = 'https://api.sandbox.paypal.com/v1/oauth2/token';



  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all users from the backend
   */
  findAll(): Observable<UserDetailDto[]> {
    return this.httpClient.get<UserDetailDto[]>(this.usersBaseUri);
  }

  /**
   * Loads specific user from the backend
   *
   * @param id of user to load
   */
  findById(id: string): Observable<UserDetailDto> {
    console.log('Load user with id: ' + id);
    return this.httpClient.get<UserDetailDto>(this.usersBaseUri + '/' + id);
  }

  /**
   * Persists user  to the backend
   *
   * @param user  to persist
   */
  create(user: UserDetailDto): Observable<UserDetailDto> {
    console.log('Create user  with name: ' + user.email);
    return this.httpClient.post<UserDetailDto>(this.usersBaseUri, user);
  }

  /**
   * Updates the given user in the persistent data storage.
   *
   * @param user user -to-update
   * @return an Observable for the updated user
   */
  update(user: UserDetailDto): Observable<UserDetailDto> {
    return this.httpClient.put<UserDetailDto>(this.usersBaseUri + '/edit/' + user.id, user);
  }

  /**
   * Delete an existing user in the system.
   *
   * @param id the id of the user to be deleted
   */
  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(this.usersBaseUri + '/' + id);
  }


  search(searchParams: UserSearch): Observable<UserDetailDto[]> {

    let params = new HttpParams();
    params = params.append('email', searchParams.email);
    if(searchParams.role){
      params = params.append('role', searchParams.role);
    }
    return this.httpClient.get<UserDetailDto[]>(this.usersBaseUri + '/search', {params})
      .pipe();
  }

  /**
   * Persists subscription to the backend
   *
   * @param subscription to persist
   */
  createSubscription(subscription: CreateSubscriptionDto): Observable<SubscriptionDetailDto> {
    console.log('Create subscription with subscriptionId: ' + subscription.subscriptionId);
    return this.httpClient.post<SubscriptionDetailDto>(`${this.usersBaseUri}/current`,subscription);
  }


  /**
   * Loads current user from the backend
   */
  getCurrentUser(): Observable<UserDetailDto> {
    return this.httpClient.get<UserDetailDto>(`${this.usersBaseUri}/current`);
  }

  /**
   * Loads all subscriptions from the backend
   */
  findAllSubscriptionsByUser(subscription: UserDetailDto): Observable<SubscriptionDetailDto[]> {

    let params = new HttpParams();
    params = params.append('email', subscription.email);
    params = params.append('id', subscription.id);
    params = params.append('role', subscription.role);

    return this.httpClient.get<SubscriptionDetailDto[]>(this.usersBaseUri + '/subscriptions', {params})
      .pipe();
   // return this.httpClient.get<SubscriptionDetailDto[]>(this.usersBaseUri + '/subscriptions')
  }

  checkSubscriptionStatus(subscriptionId: string): Observable<any> {
    return this.getPayPalAccessToken().pipe(
      switchMap((accessTokenResponse) => {
        const accessToken = accessTokenResponse.access_token;
        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`,
        });

        const url = `${this.paypalApiUrl}${subscriptionId}`;

        return this.httpClient.get<any>(url, { headers });
      })
    );
  }

  private getPayPalAccessToken(): Observable<any> {

    //TODO THIS IS A SANDBOX VERSION OF THIS CALL HERE THE CLIENT ID AND SECRET WILL ALWAYS BE THE SAME FOR LIVE VERSION IMPLEMENT CODE

    const clientId = 'YOUR_SANDBOX_CLIENT_ID';
    const secret = 'YOUR_SANDBOX_SECRET';

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + btoa(`${clientId}:${secret}`)
    });

    const body = new HttpParams()
      .set('grant_type', 'client_credentials');

    return this.httpClient.post(this.paypalTokenUrl, body.toString(), { headers });
  }


}
