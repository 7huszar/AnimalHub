import {Component, OnInit} from '@angular/core';
import { NgForm } from '@angular/forms';
import { ContactShelterDto } from "../../dtos/contact";
import { ContactService } from "../../services/contact.service";
import {ActivatedRoute} from "@angular/router";
import {Mode} from "../../enums/modes";
import {Observable} from "rxjs";
import {ShelterDto} from "../../dtos/shelter";
import {ShelterService} from "../../services/shelter.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-contact-form',
  templateUrl: './contact-form.component.html',
  styleUrls: ['./contact-form.component.scss']
})
export class ContactFormComponent implements OnInit {
  contact: ContactShelterDto = {
    to: '',
    from: '',
    name: '',
    message: ''
  };
  shelter: ShelterDto = {
    address: null,
    phoneNumber: null,
    name: '',
    donationId: "",
    id: "",
    subscriptionId: "",
    createdOn: undefined,
    email: undefined
  };
  bannerError: string | null = null;
  constructor(
    private contactService: ContactService,
    private service: ShelterService,
    private route: ActivatedRoute,
    private notification: ToastrService
  ) {
  }


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if (params.get("id")) {
        this.shelter.id = params.get("id");
        let shelterObservable: Observable<ShelterDto> = this.service.findById(this.shelter.id)
        shelterObservable.subscribe({
          next: data => {
            this.shelter = data;
          },
          error: error => {
            console.error('Error finding shelter', error);
            this.bannerError = 'Could not fetch Shelter ' + error.message;
            this.notification.error(
              error.message.message,
              'Could Not fetch Shelter'
            )
          }
        });
      }
    });
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log('Form Submitted!', this.contact);
      // Here you can handle your form submission. For example, send the form data to a server or show a success message.
      // Reset the form
      this.route.paramMap.subscribe(params => {
        if(params.get("id")) {
          this.contact.to = this.shelter.email;
          this.contactService.sendMail(this.contact).subscribe({
            next: data => {
              console.log(data);
            },
            error: error => {
              console.error('Error sending mail', error);
              this.bannerError = 'Could not send mail ' + error.message;
              this.notification.error(
                error.message.message,
                'Could Not Send Mail'
              )
            }
          });
        }
      });
      form.reset();
      this.notification.success("Mail sent successfully", "Success");
    }
  }

    protected readonly Mode = Mode;
}
