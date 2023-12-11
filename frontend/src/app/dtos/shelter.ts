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
