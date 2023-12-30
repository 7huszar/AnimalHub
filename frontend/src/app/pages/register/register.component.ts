import {Component, ViewChild} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegisterService} from "../../services/register.service";
import {AuthRequest} from "../../dtos/auth-request";
import {NotifierService} from "../../services/notifier.service";
import {HttpStatusCode} from "@angular/common/http";
import {MailRequestDto} from "../../dtos/user";
import {CircleLoadComponent} from "../../components/circle-load/circle-load.component";

@Component({
  selector: 'app-login',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  loginForm: UntypedFormGroup;
  @ViewChild('circleLoader') circleLoader: CircleLoadComponent;

  // After first submission attempt, form validation will start
  submitted: boolean = false;
  success: boolean = false;
  resent: boolean = false;
  mailRequest: MailRequestDto = {email: '', lang: ''};

  constructor(private formBuilder: UntypedFormBuilder,
              private service :RegisterService,
              private notifier :NotifierService,) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      password_repeat: ['', [Validators.required]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  register() {
    this.submitted = true;
    if (this.loginForm.valid && this.pwdMatch()) {
     this.service.register(new AuthRequest(this.loginForm.controls.username.value, this.loginForm.controls.password.value)).subscribe({
       next :data => {
        console.log("successfully registered", data);
        this.mailRequest.email = this.loginForm.controls.username.value;
        this.mailRequest.lang = 'en';
        this.success = true;
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

  /**
   * returns bootstrap is-valid class if form was once submitted
   */
  validForm() :string {
    return this.submitted ? 'is-valid' : '';
  }


  /**
   * returns bootstrap is-invalid class if form was once submitted
   */
  invalidForm() :string {
    return this.submitted ? 'is-invalid' : '';
  }

  /**
   * compares passwords of equality
   */
  pwdMatch() :boolean {
    return this.loginForm.controls.password.value === this.loginForm.controls.password_repeat.value;
  }

  resend() :void {
    this.service.requestMail(this.mailRequest).subscribe({
      next: next => {
        console.log(next);
      },
      error: error => {
        console.log(error);
      }
    });
    this.resent = true;
    setTimeout(() => {
      this.resent = false;
    }, 10_000);
    this.circleLoader.startCountdown();
  }

}
