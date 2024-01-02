import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Globals} from "../global/globals";
import {ContactShelterDto} from "../dtos/contact";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  private contactBaseUri: string = this.globals.backendUri + '/contact';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * This method is used to send an email.
   *
   * @param {ContactShelterDto} contact - The contact object containing the email details.
   * @returns {Observable} - Returns an Observable from the HttpClient post request.
   */
  sendMail(contact: ContactShelterDto): Observable<ContactShelterDto> {
    console.log('Send mail to: ' + contact.to);
    return this.httpClient.post<ContactShelterDto>(this.contactBaseUri, contact);
  }
}
