import {Species} from './species';
import {Shelter} from "./shelter";

/**
 * Interface for creating new Files
 */
export interface CreateFile {
  name: string,
  summary?: string,
  file: File
}

/**
 * Interface for getting files from backend & displaying them
 */
export interface DetailFile {
  id: number,
  name: string,
  summary?: string,
  created: Date,
  contentMimeType: string,
  contentLength: number,
  file: Blob
}

