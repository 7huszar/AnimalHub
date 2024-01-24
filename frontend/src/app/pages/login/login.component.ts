import {Component, OnInit, ViewChild} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {AuthRequest} from '../../dtos/auth-request';
import {HttpStatusCode} from "@angular/common/http";
import {NotifierService} from "../../services/notifier.service";
import {EmailFieldComponent} from "../../components/email-field/email-field.component";
import {PasswordComponent} from "../../components/password/password.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

   @ViewChild('emailField') emailField: EmailFieldComponent;
   @ViewChild('pwdField') pwdField: PasswordComponent;

  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  failedLogin: boolean = false;
  isEmailInvalid: boolean = false;
  isPwdInvalid: boolean = false;
  delayed: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private notification: NotifierService
   ) {
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.emailField.submit();
    this.pwdField.submit();
    if (this.emailField.valid() && this.pwdField.valid()) {
      const authRequest: AuthRequest = new AuthRequest(this.emailField.submit(), this.pwdField.submit());
      this.authenticateUser(authRequest);
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
        this.emailField.fail();
        this.pwdField.fail();
        let title: string = 'LOGIN.LOGIN_FAILED';
        // status == 401 / Unauthorized - wrong login credentials
         if (error.status == HttpStatusCode.Unauthorized) {
           if (error.errors?.timeOut) {
             this.notification.notifyError('DUMMY.BODY', 'DUMMY.TITLE');
           } else {
             this.notification.notifyError('LOGIN.BAD_CREDENTIALS', title);
           }
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


}
