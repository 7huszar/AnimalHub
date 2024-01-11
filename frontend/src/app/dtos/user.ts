import {ShelterDto} from "./shelter";
import {AnimalDetailDto, AnimalDto} from "./animal";

export interface UserDetailDto {
  id: string,
  email: string,
  role: string,
  shelter?: ShelterDto,
  paypalClientId?: string,
  paypalSecret?: string,
}

export interface UserSearch {
  email?: string,
  role?:string,
}

export interface MailRequestDto {
  email: string,
  lang: string
}

export interface SubscriptionDetailDto {
  id: string,
  animal?: AnimalDetailDto,
  user?: UserDetailDto,
  tier: string,
  subscriptionId: string,
}
export interface CreateSubscriptionDto {
  animal: AnimalDetailDto,
  user: UserDetailDto,
  tier: string,
  subscriptionId: string,
}


