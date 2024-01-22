import {Component, OnInit, ViewChild} from '@angular/core';
import {AnimalDto} from "../../dtos/animal";
import  {ShelterService} from "../../services/shelter.service";
import {ShelterDetailDto} from "../../dtos/shelter";
import {ActivatedRoute} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Mode} from "../../enums/modes";
import {AuthService} from "../../services/auth.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {CalendarOptions} from "@fullcalendar/core";
import listPlugin from "@fullcalendar/list";
import bootstrap5Plugin from "@fullcalendar/bootstrap5";
import enLocale from "@fullcalendar/core/locales/en-gb";
import deLocale from "@fullcalendar/core/locales/de";
import {LangChangeEvent, TranslateService} from "@ngx-translate/core";
import {AppointmentDto} from "../../dtos/appointment";
import {AppointmentStatus} from "../../enums/appointment";
import {AppointmentService} from "../../services/appointment.service";



@Component({
  selector: 'app-shelter',
  templateUrl: './shelter.component.html',
  styleUrls: ['./shelter.component.scss']
})
export class ShelterComponent implements OnInit {

  @ViewChild('calendar') calendarComponent: FullCalendarComponent;
  calendarOptions: CalendarOptions;

  protected animals: AnimalDto[] = [];
  protected shelter: ShelterDetailDto;

  constructor(private shelterService: ShelterService,
              private route: ActivatedRoute,
              private notification: ToastrService,
              public authService: AuthService,
              private translate: TranslateService,
              private appointmentService: AppointmentService
) {
  }

  ngOnInit(): void {
      this.calendarOptions = {
        height: 'auto',
        initialView: 'listWeek',
        plugins: [listPlugin, bootstrap5Plugin],
        locales: [enLocale, deLocale],
        locale: this.translate.currentLang,
        themeSystem: 'bootstrap5',
        headerToolbar: {
          left: '',
          center: '',
          right: ''
        }
      }

      this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
        this.calendarOptions.locale = this.translate.currentLang;
      });

      this.route.params.subscribe(params => {
        this.shelterService.findById(params['id']).subscribe({next: shelter => {
          this.shelter = shelter;
          this.animals = this.shelter.animals;
          this.loadUpcomingAppointments();
          },
          error: error => {
            console.error('Error finding shelter', error);
            this.notification.error("Could Not find shelter");
          }
        });
      });
  }

  private loadUpcomingAppointments(): void {
    // TODO replace load only upcoming
    this.appointmentService.findByShelterId(this.shelter.id).subscribe(appointments => {
      this.calendarComponent.getApi().removeAllEvents();
      appointments.map(this.parseAppointments).forEach(event => {
        if (event.status === AppointmentStatus.ACCEPTED) {
          this.calendarComponent.getApi().addEvent(event);
        }
      });
    });
  }

  // TODO move to util class
  private parseAppointments(appointment: AppointmentDto): any {
    let colorVal: string = null;

    if (appointment.status === AppointmentStatus.ACCEPTED) {
      colorVal = 'green';
    } else if (appointment.status === AppointmentStatus.PENDING) {
      colorVal = 'light-blue';
    } else if (appointment.status === AppointmentStatus.DECLINED) {
      colorVal = 'red';
    }

    return {
      id: appointment.id,
      start: appointment.timeFrom,
      end: appointment.timeTo,
      title: appointment.title,
      editable: false,
      color: colorVal,
      status: appointment.status
    }
  }

  showStats(): boolean {
    return this.authService.getUser().shelter && this.authService.getUser().shelter.id === this.shelter.id;
  }

  protected readonly Mode = Mode;
}
