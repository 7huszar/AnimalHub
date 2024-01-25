import {Component, EventEmitter, inject, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {
  NgbCalendar,
  NgbDate,
  NgbDateParserFormatter,
  NgbInputDatepicker,
  NgbModal,
  NgbTimeStruct
} from "@ng-bootstrap/ng-bootstrap";
import {Mode} from "../../enums/modes";
import {AppointmentStatus, AppointmentType} from "../../enums/appointment";
import {AppointmentDto} from "../../dtos/appointment";
import {AnimalDto} from "../../dtos/animal";
import {ShelterDto} from "../../dtos/shelter";
import {UserDetailDto} from "../../dtos/user";
import {AppointmentService} from "../../services/appointment.service";
import {NotifierService} from "../../services/notifier.service";

@Component({
  selector: 'app-appointment-modal',
  templateUrl: './appointment-modal.component.html',
  styleUrls: ['./appointment-modal.component.scss']
})
export class AppointmentModalComponent implements OnInit{

  @ViewChild('content') content: TemplateRef<any>;

  @Output() actionPerformed = new EventEmitter<void>();
  @Input() type: Mode;  // SHELTER | ANIMAL
  @Input() mode: Mode;  // CREATE | CONFIRM | DELETE
  @Input() header: string;
  @Input() data: any; // Binding for shelterDto | animalDto

  private modalService = inject(NgbModal);
  private appointmentService = inject(AppointmentService);
  private notifierService = inject(NotifierService)
  calendar = inject(NgbCalendar);
  formatter = inject(NgbDateParserFormatter);

  hoveredDate: NgbDate | null;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  fromTime: NgbTimeStruct;
  toTime: NgbTimeStruct;

  appointment: AppointmentDto;

  ngOnInit(): void {
    this.clear();
  }

  // Loads appointment from server
  public loadAppointment(id: string, mode: Mode) {
    this.mode = mode;

    this.appointmentService.findById(id).subscribe(value => {
      this.appointment = value;

      this.fromDate = NgbDate.from(this.formatter.parse(value.timeFrom.toISOString()));
      this.toDate = NgbDate.from(this.formatter.parse(value.timeTo.toISOString()));

      this.fromTime.hour = value.timeFrom.getHours();
      this.fromTime.minute = value.timeFrom.getMinutes();
      this.fromTime.second = value.timeFrom.getSeconds();

      this.toTime.hour = value.timeTo.getHours();
      this.toTime.minute = value.timeTo.getMinutes();
      this.toTime.second = value.timeTo.getSeconds()

      this.open();
    });
  }

  public open() {
    this.modalService.open(this.content, {ariaLabelledBy: 'appointment-title'});
  }

  public clear() {
    this.hoveredDate = null;
    this.fromDate = this.calendar.getToday();
    this.toDate = null;
    this.fromTime = { hour: 12, minute: 0, second: 0 }
    this.toTime = { hour: 12, minute: 0, second: 0 }

    this.appointment = new class implements AppointmentDto {
      animal: AnimalDto;
      id: string;
      msg: string;
      shelter: ShelterDto;
      status: AppointmentStatus;
      timeFrom: Date;
      timeTo: Date;
      title: string;
      type: AppointmentType;
      user: UserDetailDto;
    };

    if (this.type === Mode.SHELTER) {
      this.appointment.shelter = this.data;
    } else if (this.type === Mode.ANIMAL) {
      this.appointment.animal = this.data;
    }
  }

  public create() {
    const dateFrom: Date = new Date(this.fromDate.year, this.fromDate.month-1, this.fromDate.day,
      this.fromTime.hour, this.fromTime.minute, this.fromTime.second);
    const dateTo: Date = new Date(this.toDate.year, this.toDate.month-1, this.toDate.day,
      this.toTime.hour, this.toTime.minute, this.toTime.second);

    this.appointment.timeFrom = dateFrom;
    this.appointment.timeTo = dateTo;

    this.appointmentService.create(this.appointment).subscribe({next: value => {
        this.notifierService.notifySuccess('APPOINTMENT.CREATE_SUCCESS');
        this.actionFinished();
      }, error: err => {
        console.log(err)
        this.notifierService.notifyError('APPOINTMENT.CREATE_ERROR', 'APPOINTMENT.APPOINTMENTS');
      }
    });
  }

  public confirm() {
    this.appointment.status = AppointmentStatus.ACCEPTED;

    this.appointmentService.updateStatus(this.appointment).subscribe({next: value => {
        console.log('status updated!', value);
        this.notifierService.notifySuccess('APPOINTMENT.UPDATE_SUCCESS');
        this.actionFinished();
      }, error: err => {
        console.log(err);
        this.notifierService.notifyError('APPOINTMENT.UPDATE_ERROR', 'APPOINTMENT.APPOINTMENTS');
      }
    });
  }

  public decline() {
    this.appointment.status = AppointmentStatus.DECLINED;

    this.appointmentService.updateStatus(this.appointment).subscribe({next: value => {
        console.log('status updated!', value);
        this.notifierService.notifySuccess('APPOINTMENT.UPDATE_SUCCESS');
        this.actionFinished();
      }, error: err => {
        console.log(err);
        this.notifierService.notifyError('APPOINTMENT.UPDATE_ERROR', 'APPOINTMENT.APPOINTMENTS');
      }
    });
  }

  public delete() {
    this.appointmentService.delete(this.appointment.id).subscribe({
      next: () => {
        this.notifierService.notifySuccess('APPOINTMENT.DELETE_SUCCESS');
        this.actionFinished();
      }, error: err => {
        console.log(err);
        this.notifierService.notifyError('APPOINTMENT.DELETE_ERROR', 'APPOINTMENT.APPOINTMENTS');
      }
    });
  }

  actionFinished(): void {
    this.actionPerformed.emit();
    this.modalService.dismissAll();
    this.clear();
  }

  onDateSelection(date: NgbDate, datepicker: NgbInputDatepicker) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date && (this.fromDate.equals(date) || this.fromDate.before(date))) {
      this.toDate = date;
      datepicker.toggle();
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate) {
    return (
      this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate)
    );
  }

  isInside(date: NgbDate) {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return (
      date.equals(this.fromDate) ||
      (this.toDate && date.equals(this.toDate)) ||
      this.isInside(date) ||
      this.isHovered(date)
    );
  }

  validateInput(currentValue: NgbDate | null, input: string): NgbDate | null {
    const parsed = this.formatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
  }

  isValid(): boolean {

    if (this.appointment.type == null) {
      return false;
    }

    if (this.fromDate == null) {
      return false;
    }

    if (this.toDate == null) {
      return false;
    }

    if (this.fromTime.hour == null || this.fromTime.minute == null) {
      return false;
    }

    if (this.toTime.hour == null || this.toTime.minute == null) {
      return false;
    }

    if (this.appointment.title == null) {
      return false;
    }

    return this.appointment.msg != null;
  }

  isModeCreate(): boolean {
    return this.mode === Mode.CREATE;
  }

  isModeConfirm(): boolean {
    return this.mode === Mode.CONFIRM;
  }

  isModeDelete(): boolean {
    return this.mode === Mode.DELETE;
  }

  protected readonly AppointmentType = AppointmentType;
  protected readonly AppointmentStatus = AppointmentStatus;
}
