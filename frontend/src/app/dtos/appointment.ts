import {UserDetailDto} from "./user";
import {ShelterDto} from "./shelter";
import {AnimalDto} from "./animal";
import {AppointmentStatus, AppointmentType} from "../enums/appointment";

export interface AppointmentDto {
  id?: string,
  user: UserDetailDto,
  shelter: ShelterDto,
  animal: AnimalDto,
  timeFrom: Date,
  timeTo: Date,
  title: string,
  msg: string,
  type: AppointmentType,
  status: AppointmentStatus
}
