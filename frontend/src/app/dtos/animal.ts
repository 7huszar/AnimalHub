import {Species} from './species';
import {Shelter} from "./shelter";

/**
 * Interface for animals with all details
 */
export interface Animal {
  id?: number;
  name: string;
  species: Species;
  race: string;
  birthDate: Date;
  height?: number;
  weight?: number;
  shelter?: Shelter;
  createdOn?: Date
}

/**
 * Interface for listing animals in horse-search
 */
export interface AnimalListDto {
  id: number;
  name: string;
  species: Species;
  race: string;
  dateOfBirth: Date;
}

export interface AnimalCard {
  id :number;
  name :string;
}
