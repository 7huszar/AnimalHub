import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {RegisterService} from "../../../services/register.service";

@Component({
  selector: 'app-register-confirm',
  templateUrl: './register-confirm.component.html',
  styleUrls: ['./register-confirm.component.scss']
})
export class RegisterConfirmComponent implements OnInit{
  private token :string;
  constructor(private route :ActivatedRoute,
              private auth :AuthService,
              private service :RegisterService,
              private router :Router) {
  }
  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get("token");
  }

  verify() :void {
    this.service.verifyEmail(this.token).subscribe();
  }

  discard() :void {
    // TODO should definitely do smth else
    this.router.navigate(['']);
  }

}
