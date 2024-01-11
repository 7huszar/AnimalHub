import {Component, ViewChild} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../services/password.service";
import {NotifierService} from "../../services/notifier.service";
import {CircleLoadComponent} from "../../components/circle-load/circle-load.component";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent {

  @ViewChild('circleLoader') circleLoader: CircleLoadComponent;

  emailForm: UntypedFormGroup;
  submitted: boolean = false;
  sent: boolean = false;
  timeout: boolean = false;
  waitingTime: number = 60_000;

  constructor(private formBuilder: UntypedFormBuilder,
              private service: PasswordService,
              private notification: NotifierService,
              private translator: TranslateService) {
    this.emailForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]]
    });
  }

  requestPasswordReset() :void {
    this.submitted = true;
    if (this.emailForm.valid) {
      this.service.requestPasswordReset(this.emailForm.controls.email.value, this.translator.currentLang).subscribe({
        next :next => {
          this.sent = true;
          this.timeout = true;
          this.emailForm.get('email').disable();
          console.log(this.emailForm.get('email').valid)
          console.log(this.emailForm.get('email').errors)

          this.circleLoader.startCountdown();
          setTimeout(() => {
            this.timeout = false;
            this.emailForm.get('email').enable();
            console.log(this.emailForm.get('email'));
          }, this.waitingTime);
        },
        error :error => {
          console.error(error);
          this.notification.notifyStdError(error, 'PWD.FAILED');
        }
      });
    }
  }

  validation() :string {
    return this.isValid() ? 'is-valid' : 'is-invalid';
  }

  isValid() :boolean {
    return this.emailForm.get('email').disabled ? true : this.emailForm.get('email').valid;
  }
}
