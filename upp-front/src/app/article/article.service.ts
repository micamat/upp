import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ObservableLike } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  constructor(private httpClient: HttpClient) { }

  startProcess(id) {
    return this.httpClient.get("http://localhost:8080/rest-api/article/startProcess/" + id) as Observable<any>
  }

  getTasks(id) {
    return this.httpClient.get("http://localhost:8080/rest-api/article/getTasks/" + id) as Observable<any>
  }

  getFormData(taskId) {
    return this.httpClient.get("http://localhost:8080/rest-api/article/getFormData/" + taskId) as Observable<any>
  }

  completeTask(data, taskId) {
    return this.httpClient.post("http://localhost:8080/rest-api/article/completeTask/" + taskId, data) as Observable<any>
  }
}
