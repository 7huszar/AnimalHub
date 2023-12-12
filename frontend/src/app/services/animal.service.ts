import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AnimalDetailDto, AnimalListDto, AnimalSearch, CreateAnimalDto} from '../dtos/animal';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {tap} from "rxjs/operators";
import {CreateFileDto, FileDto} from "../dtos/file";

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
  findAll(): Observable<AnimalDetailDto[]> {
    return this.httpClient.get<AnimalDetailDto[]>(this.animalBaseUri);
  }

  /**
   * Loads specific animal from the backend
   *
   * @param id of animal to load
   */
  findById(id: string): Observable<AnimalDetailDto> {
    console.log('Load animal with id: ' + id);
    return this.httpClient.get<AnimalDetailDto>(this.animalBaseUri + '/' + id);
  }

  /**
   * Persists animal to the backend
   *
   * @param animal to persist
   */
  create(animal: CreateAnimalDto): Observable<AnimalDetailDto> {
    console.log('Create animal with name: ' + animal.name);
    return this.httpClient.post<AnimalDetailDto>(this.animalBaseUri, animal);
  }

  /**
   * Updates the given animal in the persistent data storage.
   *
   * @param animal       animal-to-update
   * @return an Observable for the updated animal
   */
  update(animal: AnimalDetailDto): Observable<AnimalDetailDto> {
    return this.httpClient.put<AnimalDetailDto>(this.animalBaseUri + '/' + animal.id, animal);
  }

  /**
   * deletes the animal with the given id
   *
   * @param id id
   */
  delete(id: string): Observable<any> {
    return this.httpClient.delete<void>(this.animalBaseUri+'/'+id);
  }

  getProfileImage(id: string): Observable<File> {
    return this.httpClient.get<File>(this.animalBaseUri + '/' + id + '/files/profileImg');
  }

  setProfileImage(id: string, file: CreateFileDto): Observable<FileDto> {
    const form = new FormData();
    form.append('name', file.name);
    form.append('summary', file.summary);
    form.append('file', file.file);

    return this.httpClient.post<FileDto>(this.animalBaseUri + '/' + id + '/files/profileImg', form);
  }

  updateProfileImage(id: string, file: CreateFileDto): Observable<FileDto> {
    const form = new FormData();
    form.append('name', file.name);
    form.append('summary', file.summary);
    form.append('file', file.file);

    return this.httpClient.put<FileDto>(this.animalBaseUri + '/' + id + '/files/profileImg', form);
  }

  /**
   * Searches for animals with the given parameters
   *
   * @param searchParams parameters for the search
   */
  search(searchParams: AnimalSearch): Observable<AnimalListDto[]> {
    if (searchParams.name === '') {
      delete searchParams.name;
    }
    let params = new HttpParams();
    if (searchParams.name) {
      params = params.append('name', searchParams.name);
    }
    if (searchParams.species) {
      params = params.append('species', searchParams.species);
    }
    if (searchParams.race) {
      params = params.append('race', searchParams.race);
    }
    if (searchParams.height) {
      params = params.append('height', searchParams.height);
    }
    if (searchParams.weight) {
      params = params.append('weight', searchParams.weight);
    }
    if (searchParams.shelter) {
      params = params.append('shelter', searchParams.shelter.id);
    }

    return this.httpClient.get<AnimalListDto[]>(this.animalBaseUri, { params })
      .pipe(tap(shelters => shelters.map(h => {
        h.dateOfBirth = new Date(h.dateOfBirth); // Parse date string
      })));
  }

}
