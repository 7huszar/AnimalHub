/**
 * Interface for creating new Files
 */
export interface CreateFileDto {
  name: string,
  summary?: string,
  file: File
}

/**
 * Interface for getting files from backend & displaying them
 */
export interface FileDetailDto {
  id: string,
  name: string,
  summary?: string,
  createdOn: Date,
  contentMimeType: string,
  contentLength: number,
  file: Blob
}

export interface FileDto {
  id: string,
  name: string,
  summary?: string,
  createdOn: Date,
  contentMimeType: string
}
