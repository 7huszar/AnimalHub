import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {RegisterService} from "../../../services/register.service";
import {NotifierService} from "../../../services/notifier.service";
import {HttpStatusCode} from "@angular/common/http";
import {jwtDecode} from "jwt-decode";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-register-confirm',
  templateUrl: './register-confirm.component.html',
  styleUrls: ['./register-confirm.component.scss']
})
export class RegisterConfirmComponent implements OnInit{
  private token :string;
  constructor(private route: ActivatedRoute,
              private service: RegisterService,
              private router: Router,
              private notification: NotifierService,
              private translator: TranslateService) {
  }
  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get("token");
    const decoded: any = jwtDecode(this.token);
    if (!('language' in decoded)) {
      console.error("jwt has no language issue");
      this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
      return;
    }

    const lang = decoded.language;
    if (!this.translator.getLangs().includes(lang)) {
      this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
      return;
    }
    this.translator.use(lang);
  }

  verify() :void {
    console.log(this.token);
    this.service.verifyEmail(this.token).subscribe({
      next :data => {
        this.notification.notifySuccess('REGISTER.CONFIRM.SUCCESS');
        this.router.navigate(['']);
      },
      error :error => {
        switch (error.status) {
          case HttpStatusCode.Unauthorized:
            this.notification.notifyError('REGISTER.CONFIRM.INVALID_TOKEN', 'REGISTER.CONFIRM.ERROR');
            break;
          default:
            this.notification.notifyStdError(error, 'REGISTER.CONFIRM.ERROR');
            break;
        }
      }
    });
  }

  discard() :void {
    this.notification.notifyWarn('REGISTER.CONFIRM.ABORT_WARN');
    this.router.navigate(['/']);
  }

}
