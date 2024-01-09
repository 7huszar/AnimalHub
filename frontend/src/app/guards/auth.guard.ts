import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {Mode} from "../enums/modes";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard  {

  constructor(private authService: AuthService,
              private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const access: Mode = route.data.access;

    if (access == null) {
      return true;
    }
    if (access <= this.authService.getModeRole()) {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
}
