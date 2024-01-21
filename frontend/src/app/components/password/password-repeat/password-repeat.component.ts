import {Component, ViewChild} from '@angular/core';
import {PasswordComponent} from "../password.component";

@Component({
  selector: 'app-password-repeat',
  templateUrl: './password-repeat.component.html',
  styleUrls: ['./password-repeat.component.scss']
})
export class PasswordRepeatComponent {

  submitted: boolean = false;

  @ViewChild('pwd') pwd: PasswordComponent;
  @ViewChild('pwdRep') pwdRep: PasswordComponent;
  public submit(): string {
    this.submitted = true;
    this.pwdRep.submit();
    return this.pwd.submit();
  }

  public valid(): boolean {
    return this.pwd.valid() && this.pwdRep && this.pwdMatch();
  }

  public pwdMatch(): boolean {
    return this.pwd.value() === this.pwdRep.value();
  }
}
