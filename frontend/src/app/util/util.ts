import {AppointmentDto} from "../dtos/appointment";
import {AppointmentStatus} from "../enums/appointment";

// Removes all occurrences of element in array
export function removeElement<T>(array: T[], element: T): T[] {
  return array.filter((e, i) => e !== element);
}

export function parseAppointmentToCalendarEvent(appointment: AppointmentDto): any {

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
