import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../../services/password.service";
import {ActivatedRoute} from "@angular/router";
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

  constructor(formBuilder :UntypedFormBuilder,
              private service :PasswordService,
              private route :ActivatedRoute,
              private notification :NotifierService) {
    this.passwordForm = formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  reset() :void {
    if (this.passwordForm.valid) {
      this.service.resetPassword({password: this.passwordForm.controls.password.value, token: this.token}).subscribe({
        next :next => {

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

}
