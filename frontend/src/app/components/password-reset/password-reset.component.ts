import {Component} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../services/password.service";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent {

  emailForm :UntypedFormGroup;

  constructor(private formBuilder: UntypedFormBuilder, private service :PasswordService) {
    this.emailForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]]
    });
  }

  requestPasswordReset() :void {
    if (this.emailForm.valid) {
      this.service.requestPasswordReset(this.emailForm.controls.email.value, 'en').subscribe();
    }
  }

}
