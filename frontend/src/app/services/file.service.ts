import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private filesBaseUri: string = this.globals.backendUri + '/files';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  upload(file: any): Observable<any>{
    Response<any> = this.httpClient.post(this.filesBaseUri, {name: file.name})

    var formData = new FormData();

    formData.append('file', file);

    return this.httpClient.put(response.headers("Location"), fd, {
      transformRequest: angular.identity,
      headers: {'Content-Type': undefined}
    });




}


}
