import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from '../global/globals';
import {map, Observable, switchMap} from "rxjs";
import {PreferenceDto} from "../dtos/preference";
import {AnimalDetailDto} from "../dtos/animal";
import {removeElement} from "../util/util";

@Injectable({
  providedIn: 'root'
})
export class PreferencesService {

  private preferenceBaseUri: string = this.globals.backendUri + '/preference';

  constructor(private httpClient: HttpClient, private globals: Globals) {

  }

  getRecommendedAnimals(userId: string): Observable<AnimalDetailDto[]> {
    return this.httpClient.get<AnimalDetailDto[]>(this.preferenceBaseUri + '/' + userId)
  }

  getLikedDislikedAnimals(userId: string, like: boolean): Observable<AnimalDetailDto[]> {
    console.log('Create preference: ' + like);
    return this.httpClient.get<AnimalDetailDto[]>(this.preferenceBaseUri + '/' + userId + '/' + like);
  }

  likeDislike(preference: PreferenceDto): Observable<any> {
    console.log('Create preference: ' + preference.like);
    return this.httpClient.post<PreferenceDto>(this.preferenceBaseUri, preference);
  }
}
