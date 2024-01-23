import {Component, ViewChild} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {RegisterService} from "../../services/register.service";
import {AuthRequest} from "../../dtos/auth-request";
import {NotifierService} from "../../services/notifier.service";
import {HttpStatusCode} from "@angular/common/http";
import {MailRequestDto} from "../../dtos/user";
import {CircleLoadComponent} from "../../components/circle-load/circle-load.component";
import {TranslateService} from "@ngx-translate/core";
import {EmailFieldComponent} from "../../components/email-field/email-field.component";
import {PasswordComponent} from "../../components/password/password.component";

@Component({
  selector: 'app-login',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  @ViewChild('circleLoader') circleLoader: CircleLoadComponent;
  @ViewChild('emailField') emailField: EmailFieldComponent;
  @ViewChild('pwd') pwd: PasswordComponent;

  // After first submission attempt, form validation will start
  submitted: boolean = false;
  success: boolean = false;
  resent: boolean = false;
  mailRequestTimout: number = 60_000;
  mailRequest: MailRequestDto = {email: '', lang: ''};

  constructor(private service: RegisterService,
              private notifier: NotifierService,
              private translator: TranslateService) {
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  register() {
    this.emailField.submit();
    this.pwd.submit();
    if (this.emailField.valid() && this.pwd.valid()) {
     this.service.register(new AuthRequest(this.emailField.submit(), this.pwd.submit())).subscribe({
       next :data => {
        console.log("successfully registered", data);
        this.mailRequest.email = this.emailField.submit();
        this.mailRequest.lang = this.translator.currentLang;
        this.success = true;
        this.request()
       },
       error: error => {
        console.error("failed to register", error);
        let title :string = 'REGISTER.FAILED_REGISTRATION';

        // Account already exists
        if (error.status == HttpStatusCode.Conflict) {
          this.notifier.notifyError('REGISTER.ALREADY_EXISTS', title);

        } else {
          // standard error banners
          this.notifier.notifyStdError(error, title);
        }
       }
     });
    }
  }

  request() :void {
    this.service.requestMail(this.mailRequest).subscribe({
      next: next => {
      },
      error: error => {
        console.log(error);
      }
    });
    this.resent = true;
    setTimeout(() => {
      this.resent = false;
    }, this.mailRequestTimout);
    this.circleLoader.startCountdown();
  }
}
