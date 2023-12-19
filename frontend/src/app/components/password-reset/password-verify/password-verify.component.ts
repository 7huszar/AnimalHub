import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../../services/password.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../../services/notifier.service";

@Component({
  selector: 'app-password-verify',
  templateUrl: './password-verify.component.html',
  styleUrls: ['./password-verify.component.scss']
})
export class PasswordVerifyComponent implements OnInit{

  passwordForm :UntypedFormGroup;
  private token :String;
  submitted :boolean = false;

  constructor(formBuilder :UntypedFormBuilder,
              private service :PasswordService,
              private route :ActivatedRoute,
              private router :Router,
              private notification :NotifierService) {
    this.passwordForm = formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required]]
    });
  }

  reset() :void {
    this.submitted = true;
    if (this.passwordForm.valid && this.pwdMatch()) {
      this.service.resetPassword({password: this.passwordForm.controls.password.value, token: this.token}).subscribe({
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
  }

  validation() :string {
    return this.passwordForm.valid && this.pwdMatch() ? 'is-valid' : 'is-invalid';
  }

  pwdMatch() :boolean {
    return this.passwordForm.controls.password.value === this.passwordForm.controls.passwordRepeat.value;
  }

}
