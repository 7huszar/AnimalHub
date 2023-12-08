import { Injectable } from '@angular/core';
import {HttpBackend, HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Globals} from "../global/globals";
import {AuthRequest} from "../dtos/auth-request";
import {VerificationDto} from "../dtos/register";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private http :HttpClient;
  private authBaseUri: string = this.globals.backendUri + '/register';


  constructor(handler :HttpBackend, private globals :Globals) {
    this.http = new HttpClient(handler);
  }

  // for later e-mail verification
  verifyEmail(urlToken :string) :Observable<any> {
    let verificationDto :VerificationDto= {
      token: urlToken,
      lang: 'en'
  };
    return this.http.put(this.authBaseUri, verificationDto);
  }

  register(userData :AuthRequest) :Observable<any> {
    return this.http.post(this.authBaseUri, userData);
  }
}
