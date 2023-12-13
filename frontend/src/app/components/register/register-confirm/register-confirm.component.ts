import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {RegisterService} from "../../../services/register.service";
import {NotifierService} from "../../../services/notifier.service";
import {HttpStatusCode} from "@angular/common/http";

@Component({
  selector: 'app-register-confirm',
  templateUrl: './register-confirm.component.html',
  styleUrls: ['./register-confirm.component.scss']
})
export class RegisterConfirmComponent implements OnInit{
  private token :string;
  public success :boolean = false;
  constructor(private route :ActivatedRoute,
              private service :RegisterService,
              private router :Router,
              private notifier :NotifierService) {
  }
  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get("token");
  }

  verify() :void {
    console.log(this.token);
    this.service.verifyEmail(this.token).subscribe({
      next :data => {
        this.success = true;
      },
      error :error => {
        switch (error.status) {
          case HttpStatusCode.Unauthorized:
            this.notifier.notifyError('REGISTER.CONFIRM.INVALID_TOKEN', 'REGISTER.CONFIRM.ERROR');
            break;
          default:
            this.notifier.notifyStdError(error, 'REGISTER.CONFIRM.ERROR');
            break;
        }
      }
    });
  }

  discard() :void {
    // TODO should definitely do smth else
    this.router.navigate(['/']);
  }

}
