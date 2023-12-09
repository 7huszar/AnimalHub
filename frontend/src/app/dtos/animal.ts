import {Species} from './species';
import {Shelter} from "./shelter";

/**
 * Interface for animals with all details
 */
export interface Animal {
  id?: string;
  name: string;
  species: Species;
  race: string;
  sex?: string;
  birthDate: Date;
  height?: number;
  weight?: number;
  shelter?: Shelter;
  createdOn?: Date;
}

/**
 * Interface for listing animals in horse-search
 */
export interface AnimalListDto {
  id: string;
  name: string;
  species: Species;
  race: string;
  dateOfBirth: Date;
}
