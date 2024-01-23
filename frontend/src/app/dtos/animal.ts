import {Species} from './species';
import {ShelterDto} from "./shelter";
import {Gender} from "./gender";

export interface AnimalDto {
  id: string,
  name: string,
  gender: Gender,
  englishDescription?: string,
  germanDescription?: string,
  species: Species,
  englishRace: string,
  germanRace: string,
  birthDate: Date,
  height: number,
  weight: number,
  createdOn: Date
}

export interface AnimalDetailDto {
  id: string,
  name: string,
  gender: Gender,
  englishDescription?: string,
  germanDescription?: string,
  species: Species,
  englishRace: string,
  germanRace: string,
  birthDate: Date,
  shelter: ShelterDto,
  height: number,
  weight: number,
  createdOn?: Date
}

export interface CreateAnimalDto {
  name: string,
  gender: Gender,
  englishDescription?: string,
  germanDescription?: string,
  species: Species,
  englishRace: string,
  germanRace: string,
  birthDate: Date,
  shelter: ShelterDto,
  height: number,
  weight: number
}

/**
 * Interface for listing animals in animal-search
 */
export interface AnimalListDto {
  id: string,
  name: string,
  gender: Gender,
  species: Species,
  englishRace: string,
  germanRace: string,
  dateOfBirth: Date,
  shelter: ShelterDto
}

/**
 * Interface for searching animals
 */
export interface AnimalSearch {
  name?: string,
  gender?: string,
  species?: string,
  race?: string,
  bornBefore?: Date,
  bornAfter?: Date,
  heavierThan?: number,
  lighterThan?: number,
  tallerThan?: number,
  smallerThan?: number
}
