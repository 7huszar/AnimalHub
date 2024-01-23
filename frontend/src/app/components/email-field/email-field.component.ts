import {Component} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-email-field',
  templateUrl: './email-field.component.html',
  styleUrls: ['./email-field.component.scss']
})
export class EmailFieldComponent {
  submitted: boolean = false;
  failed: boolean = false;

  loginForm: UntypedFormGroup;

  constructor(
    private formBuilder: UntypedFormBuilder,
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]],
    });

    this.loginForm.valueChanges.subscribe(() => {
      if (this.failed) {
        this.failed = false;
        this.submitted = false;
      }
    });
  }

  visual(): string {
    return this.submitted && this.loginForm.enabled ? this.loginForm.valid && !this.failed ? 'border-success' : 'border-danger' : '';
  }

  public submit(): string {
    this.submitted = true;
    return this.loginForm.get('email').value;
  }

  public valid(): boolean {
    return this.loginForm.valid;
  }

  public fail(): void {
    this.failed = true;
  }

  public disable(): void {
    this.loginForm.disable();
  }

  public enable(): void {
    this.loginForm.enable();
  }
}
