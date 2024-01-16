import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Observable} from "rxjs";
import {PreferenceDto} from "../dtos/preference";

@Injectable({
  providedIn: 'root'
})
export class PreferencesService {

  private preferenceBaseUri: string = this.globals.backendUri + '/preference';

  constructor(private httpClient: HttpClient, private globals: Globals) {

  }

  loadPreferencesByUserId(UserId: string): Observable<PreferenceDto[]> {
    return this.httpClient.get<PreferenceDto[]>(this.preferenceBaseUri + '/' + UserId)
  }
  updatePreference(preference: PreferenceDto): Observable<PreferenceDto> {
    return this.httpClient.put<PreferenceDto>(this.preferenceBaseUri + '/', preference)
  }
}


