import { Injectable } from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {HttpStatusCode} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class NotifierService {

  constructor(
    private notification :ToastrService,
    private translator :TranslateService
  ) { }

  notifyError(error :any, title :string) {
    // status == 0 means no response (not a member of HttpStatusCode)
    if (error.status === 0) {
      this.translator.get('NOTIFIER.CONNECTION_FAILED').subscribe( (translation :string) => {
        this.notification.error(translation, title);
      });
      // status == 422 / unprocessable entity - should not happen, frontend should never send not validated data
    } else if (error.status === HttpStatusCode.UnprocessableEntity) {
      this.notification.error(error.error, title);
      // status == / internal server error - this also should not happen
    } else if (error.status == HttpStatusCode.InternalServerError) {
      this.translator.get('NOTIFIER.INTERNAL_SERVER_ERROR').subscribe( (translation :string) => {
        this.notification.error(translation, title);
      });
      // this case should not happen, means error response not covered
    } else if (error.error) {
      this.notification.error(error.error, title);
    }
  }

}
