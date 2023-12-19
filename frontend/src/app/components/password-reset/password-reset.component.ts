import {Component} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../services/password.service";
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../services/notifier.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent {

  emailForm :UntypedFormGroup;
  submitted :boolean = false;
  sent :boolean = false;

  constructor(private formBuilder: UntypedFormBuilder,
              private service :PasswordService,
              private notification :NotifierService,) {
    this.emailForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]]
    });
  }

  requestPasswordReset() :void {
    this.submitted = true;
    if (this.emailForm.valid) {
      this.service.requestPasswordReset(this.emailForm.controls.email.value, 'en').subscribe({
        next :next => {
          this.sent = true;
        },
        error :error => {
          console.error(error);
          this.notification.notifyStdError(error, 'PWD.FAILED');
        }
      });
    }
  }

  validation() :string {
    return this.emailForm.valid ? 'is-valid' : 'is-invalid';
  }

}
