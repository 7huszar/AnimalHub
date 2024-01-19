import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {SubscriptionDetailDto} from "../dtos/user";


@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  private usersBaseUri: string = this.globals.backendUri + '/users';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals) {
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

  isSubscribed(animalId: string): Observable<SubscriptionDetailDto> {
    return this.httpClient.get<SubscriptionDetailDto>(`${this.usersBaseUri}/subscriptions/${animalId}`);
  }

  createSubscription(id: string): Observable<any> {
    return this.httpClient.post<string>(`${this.usersBaseUri}/subscriptions/${id}`, {});
  }
}
