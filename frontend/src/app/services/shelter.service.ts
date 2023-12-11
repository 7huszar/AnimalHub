import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Shelter} from '../dtos/shelter';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ShelterService {

  private shelterBaseUri: string = this.globals.backendUri + '/shelters';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all shelter from the backend
   */
  findAll(): Observable<Shelter[]> {
    return this.httpClient.get<Shelter[]>(this.shelterBaseUri);
  }

  /**
   * Loads specific shelter from the backend
   *
   * @param id of shelter to load
   */
  findById(id: string): Observable<Shelter> {
    console.log('Load shelter with id: ' + id);
    return this.httpClient.get<Shelter>(this.shelterBaseUri + '/' + id);
  }

  /**
   * Persists shelter  to the backend
   *
   * @param shelter  to persist
   */
  create(shelter: Shelter): Observable<Shelter> {
    console.log('Create shelter  with name: ' + shelter.name);
    return this.httpClient.post<Shelter>(this.shelterBaseUri, shelter);
  }

  /**
   * Updates the given shelter in the persistent data storage.
   *
   * @param shelter      shelter -to-update
   * @return an Observable for the updated animal
   */
  update(shelter : Shelter): Observable<Shelter> {
    return this.httpClient.put<Shelter>(
      `${this.shelterBaseUri}/${shelter .id}`,
      shelter );
  }

  /**
   * deletes the shelter with the given id
   *
   * @param id id
   */
  delete(id: string): Observable<any> {
    return this.httpClient.delete<void>(this.shelterBaseUri+'/'+id);
  }

}
