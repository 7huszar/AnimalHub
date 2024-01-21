import {Component, Input} from '@angular/core';
import {PaypalService} from "../../services/paypal.service";
import {AuthService} from "../../services/auth.service";
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['./donation.component.scss']
})
export class DonationComponent {

  @Input() receiver: string = '0';

  clicked: boolean = false;
  giveMoney: UntypedFormGroup;

  constructor(
    private service: PaypalService,
    private authService: AuthService,
    formBuilder: UntypedFormBuilder
  ) {
    this.giveMoney = formBuilder.group({
      arbitrary: ['', [Validators.pattern('\\d+(.\\d{1,2})?')]]
    });
  }

  click(): void {
    this.clicked = true;
  }

  donate() :void {
    if (this.giveMoney.valid) {
      console.log(this.giveMoney.get('arbitrary').value);
      this.service.placeOrder(this.receiver, {amount: this.giveMoney.get('arbitrary').value, donator: this.authService.getUser().id}).subscribe({
        next: next => {
          console.log(next)
          window.open(next.href);
        },
        error: error => {
          console.error(error);
        }
      });
    }
  }

  protected readonly onsubmit = onsubmit;
}
