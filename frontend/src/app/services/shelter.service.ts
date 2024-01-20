import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CreateShelterDto, ShelterDetailDto, ShelterDto, ShelterListDto, ShelterSearch} from '../dtos/shelter';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {CreateFileDto, FileDto} from "../dtos/file";
import {UserDetailDto} from "../dtos/user";



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
  findAll(): Observable<ShelterDto[]> {
    return this.httpClient.get<ShelterDto[]>(this.shelterBaseUri);
  }

  /**
   * Loads specific shelter from the backend
   *
   * @param id of shelter to load
   */
  findById(id: string): Observable<ShelterDetailDto> {
    console.log('Load shelter with id: ' + id);
    return this.httpClient.get<ShelterDetailDto>(this.shelterBaseUri + '/' + id);
  }

  /**
   * Persists shelter  to the backend
   *
   * @param shelter  to persist
   */
  create(shelter: CreateShelterDto): Observable<ShelterDto> {
    console.log('Create shelter  with name: ' + shelter.name);
    return this.httpClient.post<ShelterDto>(this.shelterBaseUri, shelter);
  }

  /**
   * Updates the given shelter in the persistent data storage.
   *
   * @param shelter      shelter -to-update
   * @return an Observable for the updated animal
   */
  update(shelter: ShelterDto): Observable<ShelterDto> {
    return this.httpClient.put<ShelterDto>(this.shelterBaseUri + '/' + shelter.id, shelter);
  }

  /**
   * Delete an existing shelter in the system.
   *
   * @param id the id of the shelter to be deleted
   */
  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(this.shelterBaseUri + '/' + id);
  }

  getProfileImage(id: string): Observable<Blob> {
    return this.httpClient.get(this.shelterBaseUri + '/' + id + '/files/profileImg', { responseType: 'blob' });
  }

  setProfileImage(id: string, file: CreateFileDto): Observable<FileDto> {
    const form = new FormData();
    form.append('name', file.name);
    form.append('summary', file.summary);
    form.append('file', file.file);

    return this.httpClient.post<FileDto>(this.shelterBaseUri + '/' + id + '/files/profileImg', form);
  }

  updateProfileImage(id: string, file: CreateFileDto): Observable<FileDto> {
    const form = new FormData();
    form.append('name', file.name);
    form.append('summary', file.summary);
    form.append('file', file.file);

    return this.httpClient.put<FileDto>(this.shelterBaseUri + '/' + id + '/files/profileImg', form);
  }


  search(searchParams: ShelterSearch): Observable<ShelterListDto[]> {

    let params = new HttpParams();
    params = params.append('name', searchParams.name);
    return this.httpClient.get<ShelterListDto[]>(this.shelterBaseUri + '/search', {params})
      .pipe();
  }

  /**
   * Assigns a user to the shelter
   *
   * @return an Observable for the updated animal
   * @param shelterId
   * @param user
   */
  assignShelter(shelterId: string, user : UserDetailDto): Observable<any> {

    return this.httpClient.put<any>(this.shelterBaseUri + '/current/' + shelterId, user);
  }


  /**
   * Checks if the user is assigned to any shelter
   */
  isAssignedTo(userid: string): Observable<ShelterDetailDto> {

    return this.httpClient.get<ShelterDetailDto>(this.shelterBaseUri +  '/current/' + userid);
  }


  /**
   * Assigns a user to the shelter
   *
   * @return an Observable for the updated animal
   * @param shelterId
   * @param user
   */
  unassignShelter(shelterId: string, user : UserDetailDto): Observable<any> {

    return this.httpClient.put<any>(this.shelterBaseUri + '/unassign/' + shelterId, user);
  }

}
