import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {AuthRequest} from '../../dtos/auth-request';
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../services/notifier.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  loginForm: UntypedFormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  failedLogin: boolean = false;
  isEmailInvalid: boolean = false;
  isPwdInvalid: boolean = false;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private router: Router,
    private notification: NotifierService
   ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.email, Validators.maxLength(254)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(255)]]
    });
    this.loginForm.valueChanges.subscribe(() => {
      if (this.failedLogin) {
        this.submitted = false;
      }
    });
    this.loginForm.get('username').valueChanges.subscribe(() => {
      this.isEmailInvalid = false;
    });
    this.loginForm.get('password').valueChanges.subscribe(() => {
      this.isPwdInvalid = false;
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.submitted = true;
    console.log("what:", this.loginForm.controls.password.errors)
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.loginForm.controls.username.value, this.loginForm.controls.password.value);
      this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
      this.failedLogin = false;
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param authRequest authentication data from the user login form
   */
  authenticateUser(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        console.log('Successfully logged in user: ' + authRequest.email);
        switch (this.authService.getUserRole()) {
          case 'ADMIN':
            this.router.navigate(['/admin']);
            break;
          case 'MEMBER':
          case 'USER':
            this.router.navigate(['/home']);
            break;
          default:
            this.router.navigate(['']);
            break;
        }
      },
      error: error => {
        console.error('Failed to log in:', error);
        let title: string = 'LOGIN.LOGIN_FAILED';
        // status == 401 / Unauthorized - wrong login credentials
         if (error.status == HttpStatusCode.Unauthorized) {
            this.notification.notifyError('LOGIN.BAD_CREDENTIALS', title);
          this.failedLogin = true;
          this.isEmailInvalid = true;
          this.isPwdInvalid = true;
        } else {
           // standard errors, we do not anticpate
           this.notification.notifyStdError(error, title);
         }
      }
    });
  }

  validForm() :string {
    return this.submitted ? 'is-valid' : '';
  }

  invalidForm() :string {
    return this.submitted ? 'is-invalid' : '';
  }

  invalidEmail() :string {
    return this.isEmailInvalid ? 'is-invalid' : '';
  }

  invalidPwd() :string {
    return this.isPwdInvalid ? 'is-invalid' : '';
  }

}
