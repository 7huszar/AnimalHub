import {Component, Input} from '@angular/core';
import {PaypalService} from "../../../services/paypal.service";
import {AuthService} from "../../../services/auth.service";
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {ShelterDetailDto} from "../../../dtos/shelter";
import {AnimalDetailDto} from "../../../dtos/animal";
import {NotifierService} from "../../../services/notifier.service";
import Decimal from "decimal.js";

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['./donation.component.scss']
})
export class DonationComponent {

  @Input() shelter: ShelterDetailDto = null;
  @Input() receiver: AnimalDetailDto = null;

  @Input() width: string = '200px';

  clicked: boolean = false;
  giveMoney: UntypedFormGroup;
  donated: boolean = false;

  constructor(
    private service: PaypalService,
    private authService: AuthService,
    private notification: NotifierService,
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
      if (new Decimal(this.giveMoney.get('euro').value).lessThan(1)) {
        return;
      }
      let cents: string = this.giveMoney.get('cent').value;
      if (cents === '') {
        cents = '00';
      }
      this.donated = true;
      this.service.placeOrder(this.shelter.id,
        {amount: this.giveMoney.get('euro').value + '.' + cents,
          donator: this.authService.getUser().id, receiver: this.receiver != null ? this.receiver.id : this.shelter.id})
        .subscribe({
        next: next => {
          this.donated = false;
          window.open(next.href);
        },
        error: error => {
          this.notification.notifyStdError(error, 'DONATION.DONATE_FAIL')
          this.donated = false;
          console.error(error);
        }
      });
    }
  }

}
