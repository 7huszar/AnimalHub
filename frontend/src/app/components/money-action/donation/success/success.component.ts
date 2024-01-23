import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PaypalService} from "../../../../services/paypal.service";

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.scss']
})
export class SuccessComponent implements OnInit {

  private token: string;
  success: boolean = false;
  failure: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private service: PaypalService
  ) {
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      this.token = params.get('token');
      this.service.captureOrder(this.token).subscribe({
        next: next => {
          console.log(next);
          this.success = true;

        },
        error: error => {
          console.error(error);
          this.failure = true;
        }
      });
    });
  }

}
