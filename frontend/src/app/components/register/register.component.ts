import {Component} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegisterService} from "../../services/register.service";
import {AuthRequest} from "../../dtos/auth-request";
import {NotifierService} from "../../services/notifier.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-login',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  loginForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;

  constructor(private formBuilder: UntypedFormBuilder,
              private router: Router,
              private service :RegisterService,
              private notifier :NotifierService,
              private translator :TranslateService) {
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
        this.router.navigate(['']);
       },
       error: error => {
        console.error("failed to register", error);
        let title :string;
        this.translator.get('REGISTER.FAILED_REGISTRATION').subscribe((translation :string) => {
          title = translation;
        });
        // standard error banners
        this.notifier.notifyError(error, title);
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

}
