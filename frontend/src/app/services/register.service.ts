import { Injectable } from '@angular/core';
import {HttpBackend, HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Globals} from "../global/globals";
import {AuthRequest} from "../dtos/auth-request";
import {VerificationDto} from "../dtos/register";
import {MailRequestDto} from "../dtos/user";
import {AuthService} from "./auth.service";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private http :HttpClient;
  private authBaseUri: string = this.globals.backendUri + '/register';


  constructor(handler :HttpBackend,
              private globals :Globals,
              private httpIntercepted: HttpClient,
              private authService: AuthService) {
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

  register(userData :AuthRequest) :Observable<string> {
    return this.http.post(this.authBaseUri, userData, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.authService.setTmpToken(authResponse))
      );
  }

  requestMail(email :MailRequestDto) :Observable<any> {

    let params = new HttpParams();
    params = params.append('email', email.email);
    params = params.append('lang', email.lang);

    return this.httpIntercepted.get(this.authBaseUri, {params});
  }
}
