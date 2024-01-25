import { Injectable } from '@angular/core';
import {Globals} from "../global/globals";
import {HttpClient} from "@angular/common/http";
import {AppointmentDto} from "../dtos/appointment";
import {map, Observable} from "rxjs";
import {AppointmentStatus} from "../enums/appointment";

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private appointmentBaseUri: string = this.globals.backendUri + '/appointments';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  findById(id: string): Observable<AppointmentDto> {
    return this.httpClient.get<AppointmentDto>(this.appointmentBaseUri + '/' + id).pipe(
      map((appointment: AppointmentDto) => {
          appointment.timeFrom = new Date(appointment.timeFrom);
          appointment.timeTo = new Date(appointment.timeTo);
          return appointment;
      })
    );
  }

  findByUserId(id: string): Observable<AppointmentDto[]> {
    return this.httpClient.get<AppointmentDto[]>(this.appointmentBaseUri + '/user/' + id).pipe(
      map((appointments: AppointmentDto[]) => {
        return appointments.map(appointment => {
          appointment.timeFrom = new Date(appointment.timeFrom);
          appointment.timeTo = new Date(appointment.timeTo);
          return appointment;
        });
      })
    );
  }

  findByShelterId(id: string): Observable<AppointmentDto[]> {
    return this.httpClient.get<AppointmentDto[]>(this.appointmentBaseUri + '/shelter/' + id).pipe(
      map((appointments: AppointmentDto[]) => {
        return appointments.map(appointment => {
          appointment.timeFrom = new Date(appointment.timeFrom);
          appointment.timeTo = new Date(appointment.timeTo);
          return appointment;
        })
      })
    );
  }

  findByShelterIdWithStatus(id: string, status: AppointmentStatus): Observable<AppointmentDto[]> {
    return this.httpClient.get<AppointmentDto[]>(this.appointmentBaseUri + '/shelter/' + id + '/' + status).pipe(
      map((appointments: AppointmentDto[]) => {
        return appointments.map(appointment => {
          appointment.timeFrom = new Date(appointment.timeFrom);
          appointment.timeTo = new Date(appointment.timeTo);
          return appointment;
        })
      })
    );
  }

  create(appointment: AppointmentDto): Observable<AppointmentDto> {
    return this.httpClient.post<AppointmentDto>(this.appointmentBaseUri, appointment);
  }

  updateStatus(appointment: AppointmentDto): Observable<AppointmentDto> {
    return this.httpClient.put<AppointmentDto>(this.appointmentBaseUri + '/' + appointment.id, appointment);
  }

  delete(id: string): Observable<any> {
    return this.httpClient.delete<void>(this.appointmentBaseUri + '/' + id);
  }
}
