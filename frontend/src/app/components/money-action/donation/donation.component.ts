import {Component, Input} from '@angular/core';
import {PaypalService} from "../../../services/paypal.service";
import {AuthService} from "../../../services/auth.service";
import {UntypedFormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {ShelterDetailDto} from "../../../dtos/shelter";
import {AnimalDetailDto} from "../../../dtos/animal";

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
      this.donated = true;
      this.service.placeOrder(this.shelter.id,
        {amount: this.giveMoney.get('euro').value + '.' + cents,
          donator: this.authService.getUser().id, receiver: this.receiver != null ? this.receiver.id : this.shelter.id})
        .subscribe({
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
