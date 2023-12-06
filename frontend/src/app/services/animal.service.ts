import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Globals} from "../global/globals";
import {AnimalListDto} from "../dtos/animal";

@Injectable({
  providedIn: 'root'
})
export class AnimalService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(private http: HttpClient, private globals: Globals) {
  }

  // for later e-mail verification
  finalAll() :Observable<AnimalListDto[]> {
    return this.http.get<AnimalListDto[]>(this.authBaseUri);
  }
}
