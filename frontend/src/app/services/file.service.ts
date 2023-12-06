import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {CreateFile} from "../dtos/file";

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private filesBaseUri: string = this.globals.backendUri + '/files';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  create(createFile: CreateFile): void {
    console.log('Create file with name: ' + createFile.name);

    const formData = new FormData();

    formData.append('name', createFile.name);
    formData.append('summary', createFile.summary);
    formData.append('file', createFile.file);

    const headers = new HttpHeaders();
    // Ensure that the Content-Type header is set to 'multipart/form-data'
    headers.set('Content-Type', 'multipart/form-data');

    this.httpClient.post(this.filesBaseUri, formData).subscribe((value) => {
      console.log("successfully uploaded file");
    });

  }


  /**
   * Loads specific file from the backend
   *
   * @param id of file to load
   */
  // findById(id: number): DetailFile {
  //   console.log('Load file with id: ' + id);
  //   let file: DetailFile = {
  //     id: id,
  //     name: "",
  //     summary: "",
  //     created: null,
  //     fileBlob: null,
  //     contentMimeType: ""
  //   } as DetailFile;
  //   this.httpClient.get(this.filesBaseUri + '/' + id, {responseType: "blob"})
  //     .subscribe(data => {
  //       file.fileBlob = new Blob([data], {type: data.type});
  //       file.contentMimeType = data.type;
  //     });
  //
  //   return file;
  // }


}
