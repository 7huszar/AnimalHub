import { Injectable } from '@angular/core';
import {HttpBackend, HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Globals} from "../global/globals";
import {AuthRequest} from "../dtos/auth-request";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private http :HttpClient;
  private authBaseUri: string = this.globals.backendUri + '/register';

  constructor(handler :HttpBackend, private globals :Globals) {
    this.http = new HttpClient(handler);
  }

  verifyEmail(token :string) :Observable<any> {
    return this.http.get(this.authBaseUri, {
      headers: {'Authorization': 'Bearer ' + token}
    });
  }

  register(userData :AuthRequest) :Observable<any> {
    return this.http.post(this.authBaseUri, userData);
  }

}
