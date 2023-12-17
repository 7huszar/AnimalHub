import { Injectable } from '@angular/core';
import {HttpBackend, HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PasswordService {

  private baseUri: string = this.globals.backendUri + '/password';
  private http :HttpClient;

  constructor(handler :HttpBackend, private globals: Globals) {
    this.http = new HttpClient(handler);
  }

  public requestPasswordReset(email :string, lang :string) :Observable<any> {
    let params = new HttpParams();
    params = params.append('email', email);
    params = params.append('lang', lang);

    return this.http.get(this.baseUri, {params});
  }
}
