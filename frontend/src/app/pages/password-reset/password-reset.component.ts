import {Component, ViewChild} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../services/password.service";
import {NotifierService} from "../../services/notifier.service";
import {CircleLoadComponent} from "../../components/circle-load/circle-load.component";
import {TranslateService} from "@ngx-translate/core";
import {EmailFieldComponent} from "../../components/email-field/email-field.component";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent {

  @ViewChild('circleLoader') circleLoader: CircleLoadComponent;
  @ViewChild('emailField') emailField: EmailFieldComponent;

  submitted: boolean = false;
  sent: boolean = false;
  timeout: boolean = false;
  waitingTime: number = 60_000;

  constructor(private service: PasswordService,
              private notification: NotifierService,
              private translator: TranslateService) {
  }

  requestPasswordReset() :void {
    this.submitted = true;
    if (this.emailField.valid()) {
      this.service.requestPasswordReset(this.emailField.submit(), this.translator.currentLang).subscribe({
        next :next => {
          this.sent = true;
          this.timeout = true;
          this.emailField.disable();

          this.circleLoader.startCountdown();
          setTimeout(() => {
            this.timeout = false;
            this.emailField.enable();
          }, this.waitingTime);
        },
        error :error => {
          console.error(error);
          this.notification.notifyStdError(error, 'PWD.FAILED');
        }
      });
    }
  }
}
