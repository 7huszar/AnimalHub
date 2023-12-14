import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  logout() :void {
    this.authService.logoutUser();
  }

  isMemberWithShelter(): boolean {
    return this.authService.isMember() && this.authService.getUser().shelter !== null;
  }

  getShelterId(): string {
    if (!this.isMemberWithShelter())
      return null;

    return this.authService.getUser().shelter.id;
  }
}
