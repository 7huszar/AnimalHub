import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Animal} from '../dtos/animal';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class AnimalService {

  private animalBaseUri: string = this.globals.backendUri + '/animals';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all animals from the backend
   */
  findAll(): Observable<Animal[]> {
    return this.httpClient.get<Animal[]>(this.animalBaseUri);
  }

  /**
   * Loads specific animal from the backend
   *
   * @param id of animal to load
   */
  findById(id: string): Observable<Animal> {
    console.log('Load animal with id: ' + id);
    return this.httpClient.get<Animal>(this.animalBaseUri + '/' + id);
  }

  /**
   * Persists animal to the backend
   *
   * @param animal to persist
   */
  create(animal: Animal): Observable<Animal> {
    console.log('Create animal with name: ' + animal.name);
    return this.httpClient.post<Animal>(this.animalBaseUri, animal);
  }

  /**
   * Updates the given animal in the persistent data storage.
   *
   * @param animal       animal-to-update
   * @return an Observable for the updated animal
   */
  update(animal: Animal): Observable<Animal> {
    return this.httpClient.put<Animal>(
      `${this.animalBaseUri}/${animal.id}`,
      animal);
  }

  /**
   * deletes the animal with the given id
   *
   * @param id id
   */
  delete(id: string): Observable<any> {
    return this.httpClient.delete<void>(this.animalBaseUri+'/'+id);
  }
}
