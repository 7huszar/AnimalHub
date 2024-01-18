import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {PaypalService} from "../../../services/paypal.service";

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.scss']
})
export class SuccessComponent implements OnInit {

  private routeSub: Subscription;
  private token: string;

  constructor(
    private route: ActivatedRoute,
    private service: PaypalService
  ) {
  }

  ngOnInit() {
    this.routeSub = this.route.queryParamMap.subscribe(params => {
      this.token = params.get('token');
      this.service.captureOrder(this.token).subscribe({
        next: next => {
          console.log(next);
        },
        error: error => {
          console.error(error);
        }
      });
    });
  }

}
