import {ShelterDto} from "./shelter";

export interface UserDetailDto {
  id: string,
  email: string,
  role: string,
  shelter?: ShelterDto
}

export interface UserSearch {
  email?: string,
  role?:string,
}
