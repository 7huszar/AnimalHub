import {Component, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../../services/password.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-password-verify',
  templateUrl: './password-verify.component.html',
  styleUrls: ['./password-verify.component.scss']
})
export class PasswordVerifyComponent implements OnInit{

  passwordForm :UntypedFormGroup;
  private token :String;

  constructor(formBuilder :UntypedFormBuilder, private service :PasswordService, private route :ActivatedRoute) {
    this.passwordForm = formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  reset() :void {
    if (this.passwordForm.valid) {
      this.service.resetPassword({password: this.passwordForm.controls.password.value, token: this.token}).subscribe();
    }
  }

  ngOnInit() {
    this.token = this.route.snapshot.paramMap.get('token');
  }

}
