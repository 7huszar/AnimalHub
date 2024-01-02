import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ContactShelterDto } from "../../dtos/contact";
import { ContactService } from "../../services/contact.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-contact-form',
  templateUrl: './contact-form.component.html',
  styleUrls: ['./contact-form.component.scss']
})
export class ContactFormComponent {
  contact: ContactShelterDto = {
    to: '',
    from: '',
    name: '',
    message: ''
  };
  constructor(
    private contactService: ContactService,
    private route: ActivatedRoute,
  ) {
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log('Form Submitted!', this.contact);
      // Here you can handle your form submission. For example, send the form data to a server or show a success message.
      // Reset the form
      this.route.paramMap.subscribe(params => {
        if(params.get("id")) {
          this.contact.to = params.get("id");
          this.contactService.sendMail(this.contact).subscribe({
            next: data => {
              console.log(data);
            },
            error: error => {
              console.error('Error sending mail', error);
            }
          });
        }
      });
      form.reset();
    }
  }
}
