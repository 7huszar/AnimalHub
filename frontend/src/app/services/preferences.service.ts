import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Observable} from "rxjs";
import {PreferenceDto} from "../dtos/preference";
import {AnimalDetailDto} from "../dtos/animal";

@Injectable({
  providedIn: 'root'
})
export class PreferencesService {

  private preferenceBaseUri: string = this.globals.backendUri + '/preference';


  constructor(private httpClient: HttpClient, private globals: Globals) {

  }

  getLikedDislikedAnimals(userId: string, like: boolean): Observable<AnimalDetailDto[]> {
    console.log('Create preference: ' + like);
    return this.httpClient.get<AnimalDetailDto[]>(this.preferenceBaseUri + '/' + userId + '/' + like);
  }

  /*loadPreferencesByUserId(userId: string): Observable<PreferenceDto[]> {
    return this.httpClient.get<PreferenceDto[]>(this.preferenceBaseUri + '/' + userId)
  }
  updatePreference(preference: PreferenceDto): Observable<PreferenceDto> {
    return this.httpClient.put<PreferenceDto>(this.preferenceBaseUri + '/', preference)
  }

   */

  likeDislike(preference: PreferenceDto): Observable<PreferenceDto> {
    console.log('Create preference: ' + preference.like);
    return this.httpClient.post<PreferenceDto>(this.preferenceBaseUri, preference);
  }




}


