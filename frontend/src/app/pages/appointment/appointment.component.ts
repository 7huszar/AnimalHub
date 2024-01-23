import {Component, OnInit, ViewChild} from '@angular/core';
import {CalendarOptions} from "@fullcalendar/core";
import dayGridPlugin from '@fullcalendar/daygrid';
import bootstrap5Plugin from '@fullcalendar/bootstrap5';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import enLocale from '@fullcalendar/core/locales/en-gb';
import deLocale from '@fullcalendar/core/locales/de';
import {LangChangeEvent, TranslateService} from "@ngx-translate/core";
import {ActivatedRoute} from "@angular/router";
import {Mode} from "../../enums/modes";
import {AppointmentService} from "../../services/appointment.service";
import {AuthService} from "../../services/auth.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {AppointmentStatus} from "../../enums/appointment";
import {AppointmentDto} from "../../dtos/appointment";
import {AppointmentModalComponent} from "../../components/appointment-modal/appointment-modal.component";

@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

  @ViewChild('appointmentModal') appointmentModal: AppointmentModalComponent;
  @ViewChild('calendar') calendarComponent: FullCalendarComponent;
  calendarOptions: CalendarOptions;
  mode: Mode;

  constructor(private translate: TranslateService, private route: ActivatedRoute,
              private authService: AuthService,
              private appointmentService: AppointmentService) {
  }

  ngOnInit(): void {
    this.calendarOptions = {
      height: 'auto',
      contentHeight: 'auto',
      initialView: 'dayGridMonth',
      plugins: [dayGridPlugin, timeGridPlugin, listPlugin, bootstrap5Plugin],
      locales: [enLocale, deLocale],
      locale: this.translate.currentLang,
      themeSystem: 'bootstrap5',
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
      },
      eventClick: info => {
        this.openModal(info);
      },
      weekNumbers: true,
      dayMaxEvents: true
    };

    this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.calendarOptions.locale = this.translate.currentLang;
    });

    this.route.data.subscribe(data => {
      this.mode = data.mode;
      this.loadAppointments();
    });
  }

  private loadAppointments(): void {
    if (this.mode === Mode.USER) {
      this.appointmentService.findByUserId(this.authService.getUser().id).subscribe(appointments => {
        this.calendarComponent.getApi().removeAllEvents();
        appointments.map(this.parseAppointments).forEach(event => {
          this.calendarComponent.getApi().addEvent(event);
        });
      });
    } else if (this.mode === Mode.SHELTER) {
      this.appointmentService.findByShelterId(this.authService.getUserShelter().id).subscribe(appointments => {
        this.calendarComponent.getApi().removeAllEvents();
        appointments.map(this.parseAppointments).forEach(event => {
          this.calendarComponent.getApi().addEvent(event);
        });
      });
    }
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
      color: colorVal
    }
  }

  openModal(info: any) {
    if (this.mode === Mode.USER) {
      this.appointmentModal.loadAppointment(info.event.id, Mode.DELETE);
    } else if (this.mode === Mode.SHELTER) {
      this.appointmentModal.loadAppointment(info.event.id, Mode.CONFIRM);
    }
  }

  isModeUser(): boolean {
    return this.mode === Mode.USER;
  }

  isModeShelter(): boolean {
    return this.mode === Mode.SHELTER;
  }
}
