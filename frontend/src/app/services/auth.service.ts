import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {jwtDecode} from 'jwt-decode';
import {Globals} from '../global/globals';
import {UserDetailDto} from "../dtos/user";
import {ShelterDto} from "../dtos/shelter";
import {Mode} from "../enums/modes";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';
  private userUri: string = this.globals.backendUri + '/users/current';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  private loadUser(): void {
    this.httpClient.get<UserDetailDto>(this.userUri).subscribe({
      next: userDto => {
        localStorage.setItem('authUser', JSON.stringify(userDto));
      }, error: err => {
        console.error('Error loading user.', err);
      }
    });
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && this.getUserRole() != 'UNDEFINED' && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
    localStorage.removeItem('authUser');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  getUser(): UserDetailDto {
    return JSON.parse(localStorage.getItem('authUser')) as UserDetailDto;
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwtDecode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ADMIN_ROLE')) {
        return 'ADMIN';
      } else if (authInfo.includes('MEMBER_ROLE')) {
        return 'MEMBER';
      } else if (authInfo.includes('USER_ROLE')) {
        return 'USER';
      } else if (authInfo.includes('INACTIVE_ROLE')) {
        return 'INACTIVE';
      }
    }
    return 'UNDEFINED';
  }

  getModeRole(): Mode {
    const role: string = this.getUserRole();
    if (role === 'ADMIN') {
      return Mode.ADMIN;
    }
    if (role === 'MEMBER') {
      return Mode.MEMBER;
    }
    if (role === 'USER') {
      return Mode.USER;
    }
    if (role === 'INACTIVE') {
      return Mode.INACTIVE;
    }
    return null;
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
    this.loadUser();
  }

  private getTokenExpirationDate(token: string): Date {

    const decoded: any = jwtDecode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  isAdmin() :boolean {
    return this.getUserRole() === 'ADMIN';
  }

  isMember() :boolean {
    const decoded: any = jwtDecode(this.getToken());
    return decoded.rol.includes('MEMBER_ROLE');
  }

  isUser() :boolean {
    const decoded: any = jwtDecode(this.getToken());
    return decoded.rol.includes('USER_ROLE');
  }

  isMemberOf(shelterId: string): boolean {
    if (!this.isMember())
      return false;

    const user = this.getUser();

    return user !== null && user.shelter !== null && user.shelter.id === shelterId;
  }

  getUserShelter(): ShelterDto {
    if (!this.isMember())
      return null;

    if (this.getUser().shelter !== null) {
      return this.getUser().shelter;
    }
  }
}
