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
    return this.httpClient.delete<void>(this.animalBaseUri + '/' + id);
  }

  getProfileImage(id: string): Observable<Blob> {
    return this.httpClient.get(this.animalBaseUri + '/' + id + '/files/profileImg', {responseType: 'blob'});
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

  getContents(id: string): Observable<FileDto[]> {
    return this.httpClient.get<FileDto[]>(this.animalBaseUri + '/' + id + '/files/content');
  }

  getContent(animalId: string, userId: string, fileId: string): Observable<Blob> {
    return this.httpClient.get(this.animalBaseUri + '/' + animalId + '/' + userId + '/files/content/' + fileId, {responseType: 'blob'});
  }

  addContent(id: string, file: CreateFileDto): Observable<FileDto> {
    const form = new FormData();
    form.append('name', file.name);
    form.append('summary', file.summary);
    form.append('file', file.file);

    return this.httpClient.post<FileDto>(this.animalBaseUri + '/' + id + '/files/content', form);
  }

  deleteContent(id: string, fileId: string): Observable<void> {
    return this.httpClient.delete<void>(this.animalBaseUri + '/' + id + '/files/content/' + fileId);
  }

  /**
   * Searches for animals with the given parameters
   *
   * @param searchParams parameters for the search
   */
  search(searchParams: AnimalSearch): Observable<AnimalListDto[]> {
    let params = new HttpParams();

    console.log("searchParams:", searchParams);

    if (searchParams.name && searchParams.name !== "") {
      params = params.set('name', searchParams.name);
    }
    if (searchParams.gender && searchParams.gender !== "") {
      params = params.set('gender', searchParams.gender);
    }
    if (searchParams.species && searchParams.species !== "") {
      params = params.set('species', searchParams.species);
    }
    if (searchParams.race && searchParams.race !== "") {
      params = params.set('race', searchParams.race);
    }
    if (searchParams.bornBefore !== undefined && searchParams.bornBefore != null) {
      params = params.set('bornBefore', searchParams.bornBefore.toString());
    }
    if (searchParams.bornAfter !== undefined && searchParams.bornAfter != null) {
      params = params.set('bornAfter', searchParams.bornAfter.toString());
    }
    if (searchParams.heavierThan !== undefined && searchParams.heavierThan != null) {
      params = params.set('heavierThan', searchParams.heavierThan);
    }
    if (searchParams.lighterThan !== undefined && searchParams.lighterThan != null) {
      params = params.set('lighterThan', searchParams.lighterThan);
    }
    if (searchParams.tallerThan !== undefined && searchParams.tallerThan != null) {
      params = params.set('tallerThan', searchParams.tallerThan.valueOf());
    }
    if (searchParams.smallerThan !== undefined && searchParams.smallerThan != null) {
      params = params.set('smallerThan', searchParams.smallerThan);
    }

    return this.httpClient.get<AnimalListDto[]>(this.animalBaseUri + "/search", {params})
      .pipe(
        tap(animals => animals.forEach(animal => {
          animal.dateOfBirth = new Date(animal.dateOfBirth); // Parse date string
        }))
      );
  }


}
