import {AnimalDto} from "./animal";


/**
 * Interface for shelters with all details
 */

export interface CreateShelterDto {
  name: string,
  address: string,
  donationId: string,
  phoneNumber: string,
  email: string
  paypalClientId?: string,
}

export interface ShelterDetailDto {
  id: string,
  name: string,
  address: string,
  donationId: string,
  subscriptionId: string,
  phoneNumber: string,
  email: string,
  animals?: AnimalDto[],
  createdOn: Date
  paypalClientId?: string,
}

export interface ShelterDto {
  id: string,
  name: string,
  donationId: string,
  subscriptionId: string,
  address: string,
  phoneNumber: string,
  email: string,
  createdOn: Date
  paypalClientId?: string,
  paypalSecret?: string,
}

export interface ShelterListDto {
  profilePicture: File;
  id: string;
  name: string;
  donationId: string;
  subscriptionId: string;
  address: String;
  phoneNumber: String;
  animals: Set<AnimalDto>;
}

export interface ShelterSearch {
  name?: string;
}

export class IdOnlyShelter implements ShelterDto {
  id: string;
  address: string;
  donationId: string;
  subscriptionId: string;
  email: string;
  name: string;
  phoneNumber: string;
  createdOn: Date;

  public constructor(id: string) {
    this.id = id;
  }
}

export interface DonationListDto {
  amount: number;
  date: Date;
}
