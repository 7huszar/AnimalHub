import {Species} from './species';
import {ShelterDto} from "./shelter";


export interface AnimalDto {
  id: string,
  name: string,
  species: Species,
  race: string,
  birthDate: Date,
  height: number,
  weight: number,
  createdOn: Date
}

export interface AnimalDetailDto {
  id: string,
  name: string,
  species: Species,
  race: string,
  birthDate: Date,
  shelter: ShelterDto,
  height: number,
  weight: number,
  createdOn: Date
}

export interface CreateAnimalDto {
  name: string,
  species: Species,
  race: string,
  birthDate: Date,
  shelter: ShelterDto,
  height: number,
  weight: number
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

/**
 * Interface for searching animals
 */
export interface AnimalSearch {
  name?: string;
  species?: Species;
  race?: string;
  birthDate?: Date;
  height?: number;
  weight?: number;
  shelter?: ShelterDto;
}
