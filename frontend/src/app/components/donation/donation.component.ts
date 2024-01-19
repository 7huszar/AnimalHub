import {Component, Input} from '@angular/core';
import {PaypalService} from "../../services/paypal.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['./donation.component.scss']
})
export class DonationComponent {

  @Input() amount: string = '0.0';
  @Input() receiver: string = '0';

  constructor(
    private service: PaypalService,
    private authService: AuthService
  ) {
  }

  donate() :void {
    console.log({amount: this.amount});
    this.service.placeOrder(this.receiver, {amount: this.amount, donator: this.authService.getUser().id}).subscribe({
      next: next => {
        console.log('donated ' + this.amount + '€ to ' + this.receiver);
        console.log(next.href);
        window.open(next.href);
      },
      error: error => {
        console.error(error);
      }
    });
  }
}
