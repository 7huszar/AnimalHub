import {Animal} from "./animal";


/**
 * Interface for shelters with all details
 */
export interface Shelter {
  id?: string;
  name: string;
  address: string;
  phoneNumber: string;
  email: string;
  animals?: Animal[];
  createdOn?: Date
}

export interface ShelterListDto {
  profilePicture:File;
  id: string;
  name: string;
  address: String;
  phoneNumber: String;
  animals: Set<Animal>;
}

export interface ShelterSearch {
  name?: string;
}

export class IdOnlyShelter implements Shelter {
  id: string;
  address: string;
  email: string;
  name: string;
  phoneNumber: string;

  public constructor(id: string) {
    this.id = id;
  }




}
