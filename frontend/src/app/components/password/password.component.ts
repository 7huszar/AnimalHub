import {Component, Input} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss']
})
export class PasswordComponent {
  submitted: boolean = false;
  failed: boolean = false;
  @Input() higherCause: boolean = false;

  @Input() pwdField: string = 'password';
  @Input() eye: boolean = true;
  loginForm: UntypedFormGroup;
  constructor(
    private formBuilder: UntypedFormBuilder,
  ) {
    this.loginForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(255)]]
    });

    this.loginForm.valueChanges.subscribe(() => {
      if (this.failed) {
        this.submitted = false;
        this.failed = false;
      }
    });

  }


  togglePwd(): void {
    if (this.pwdField === 'password') {
      this.pwdField = 'text';
    } else {
      this.pwdField = 'password';
    }
  }

  visual(): string {
    return this.submitted ? (this.loginForm.valid && this.higherCause) && !this.failed ? 'border-success' : 'border-danger' : '';
  }

  public submit(): string {
    this.submitted = true;
    return this.loginForm.get('password').value;
  }

  public valid(): boolean {
    return this.loginForm.valid;
  }

  public fail(): void {
    this.failed = true;
  }

  public visible(): string {
    return this.pwdField;
  }

  public value(): string {
    return this.loginForm.get('password').value;
  }
}
