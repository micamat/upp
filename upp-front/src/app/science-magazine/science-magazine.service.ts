import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScienceMagazineService {

  constructor(private httpClient: HttpClient) { }

  getMagazineFormData() {
    return this.httpClient.get('http://localhost:8080/rest-api/magazine-form-data') as Observable<any>
  }

  addMagazine(magazine, taskId, processInstanceId, editorId) {
    return this.httpClient.post("http://localhost:8080/rest-api/add-magazine/" + taskId + "/" + processInstanceId + "/" + editorId, magazine) as Observable<any>
  }

  findEditors(scienceFields) {
    return this.httpClient.get("http://localhost:8080/rest-api/editors/" + scienceFields) as Observable<any>
  }

  findReviewers(scienceFields) {
    return this.httpClient.get("http://localhost:8080/rest-api/reviewers/" + scienceFields) as Observable<any>
  }

  completeTaskAddEditorsAndReviewers(formFieldData, taskId, magazineId) {
    return this.httpClient.post("http://localhost:8080/rest-api/completeEditorTask/" + taskId + "/" + magazineId, formFieldData) as Observable<any>
  }

  updateMagazine(magazine) {
    return this.httpClient.put("http://localhost:8080/rest-api/updateMagazine", magazine) as Observable<any>
  }

}
