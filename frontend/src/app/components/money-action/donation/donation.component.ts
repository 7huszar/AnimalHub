import {Component, Input} from '@angular/core';
import {PaypalService} from "../../../services/paypal.service";
import {AuthService} from "../../../services/auth.service";
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {ShelterDetailDto} from "../../../dtos/shelter";

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['./donation.component.scss']
})
export class DonationComponent {

  @Input() receiver: ShelterDetailDto = null;
  @Input() width: string = '200px';

  clicked: boolean = false;
  giveMoney: UntypedFormGroup;

  constructor(
    private service: PaypalService,
    private authService: AuthService,
    formBuilder: UntypedFormBuilder
  ) {
    this.giveMoney = formBuilder.group({
      euro: ['', [Validators.pattern('\\d+')]],
      cent: ['', Validators.pattern('\\d*')]
    });
  }

  click(): void {
    this.clicked = true;
  }

  donate() :void {
    if (this.giveMoney.valid) {
      let cents: string = this.giveMoney.get('cent').value;
      if (cents === '') {
        cents = '00';
      }
      this.service.placeOrder(this.receiver.id,
        {amount: this.giveMoney.get('euro').value + '.' + cents,
          donator: this.authService.getUser().id}).subscribe({
        next: next => {
          window.open(next.href);
        },
        error: error => {
          console.error(error);
        }
      });
    }
  }

}
