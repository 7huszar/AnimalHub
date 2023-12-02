import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private userType :string;

  constructor(public authService: AuthService) { }

  ngOnInit() {
      this.userType = this.authService.getUserRole();
  }

  isMember() :boolean {
    return this.userType === 'MEMBER';
  }

}
