import {Component, OnInit, ViewChild} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../../services/password.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../../services/notifier.service";
import {jwtDecode} from "jwt-decode";
import {TranslateService} from "@ngx-translate/core";
import {PasswordComponent} from "../../../components/password/password.component";

@Component({
  selector: 'app-password-verify',
  templateUrl: './password-verify.component.html',
  styleUrls: ['./password-verify.component.scss']
})
export class PasswordVerifyComponent implements OnInit {

  @ViewChild('pwd') pwd: PasswordComponent;

  private token: string;
  submitted: boolean = false;

  constructor(private service: PasswordService,
              private route: ActivatedRoute,
              private router: Router,
              private notification: NotifierService,
              private translator: TranslateService) {
  }

  reset() :void {
    this.submitted = true;
    this.pwd.submit();
    if (this.pwd.valid()) {
      this.service.resetPassword({password: this.pwd.value(), token: this.token}).subscribe({
        next :next => {
          this.notification.notifySuccess('PWD.RESET.SUCCESS');
          this.router.navigate(['/']);
        },
        error :error => {
          console.error(error);
          if (error.status == HttpStatusCode.Unauthorized) {
            this.notification.notifyError('PWD.RESET.REQUEST_NEW', 'PWD.RESET.FAILED');
          } else {
            this.notification.notifyStdError(error, 'PWD.RESET.FAILED');
          }
        }
      });
    }
  }

  ngOnInit() {
    this.token = this.route.snapshot.paramMap.get('token');
    const decoded: any = jwtDecode(this.token);
    if (!('language' in decoded)) {
      this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
    }
    const lang = decoded.language;
    if (!this.translator.getLangs().includes(lang)) {
      this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
    }
    this.translator.use(lang);
  }
}
