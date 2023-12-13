import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {UserDetailDto, UserSearch} from "../dtos/user";



@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all users from the backend
   */
  findAll(): Observable<UserDetailDto[]> {
    return this.httpClient.get<UserDetailDto[]>(this.usersBaseUri);
  }

  /**
   * Loads specific user from the backend
   *
   * @param id of user to load
   */
  findById(id: string): Observable<UserDetailDto> {
    console.log('Load user with id: ' + id);
    return this.httpClient.get<UserDetailDto>(this.usersBaseUri + '/' + id);
  }

  /**
   * Persists user  to the backend
   *
   * @param user  to persist
   */
  create(user: UserDetailDto): Observable<UserDetailDto> {
    console.log('Create user  with name: ' + user.email);
    return this.httpClient.post<UserDetailDto>(this.usersBaseUri, user);
  }

  /**
   * Updates the given user in the persistent data storage.
   *
   * @param user user -to-update
   * @return an Observable for the updated user
   */
  update(user: UserDetailDto): Observable<UserDetailDto> {
    return this.httpClient.put<UserDetailDto>(this.usersBaseUri + '/edit/' + user.id, user);
  }

  /**
   * Delete an existing user in the system.
   *
   * @param id the id of the user to be deleted
   */
  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(this.usersBaseUri + '/' + id);
  }


  search(searchParams: UserSearch): Observable<UserDetailDto[]> {

    let params = new HttpParams();
    params = params.append('email', searchParams.email);
    if(searchParams.role){
      params = params.append('role', searchParams.role);
    }
    return this.httpClient.get<UserDetailDto[]>(this.usersBaseUri + '/search', {params})
      .pipe();
  }

}
