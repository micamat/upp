import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private httpClient: HttpClient) { }

  getFormData() {
    return this.httpClient.get('http://localhost:8080/rest-api/form-data') as Observable<any>
  }

  apply(user, taskId, processInstanceId) {
    return this.httpClient.post('http://localhost:8080/rest-api/apply/'.concat(taskId) + "/".concat(processInstanceId), user) as Observable<any>
  }

  mailRequested(processInstanceId) {
    return this.httpClient.get("http://localhost:8080/rest-api/mail-request/" + processInstanceId) as Observable<any>
  }

  mailConfirmed(processInstanceId) {
    return this.httpClient.get("http://localhost:8080/rest-api/mail-confirm/" + processInstanceId) as Observable<any>
  }

  loggedUser() {
    return this.httpClient.get("http://localhost:8080/rest-api/loggedUser") as Observable<any>
  }

  logout(processInstanceId) {
    return this.httpClient.get("http://localhost:8080/rest-api/logout/" + processInstanceId) as Observable<any>
  }

  login(processInstanceId, userCredentials) {
    return this.httpClient.post("http://localhost:8080/rest-api/login/" + processInstanceId, userCredentials) as Observable<any>
  }

  getTasks(userId) {
    return this.httpClient.get("http://localhost:8080/rest-api/getTasks/" + userId) as Observable<any>
  }

  findInstanceIdByUser(userId) {
    return this.httpClient.get("http://localhost:8080/rest-api/instance/" + userId) as Observable<any>
  }

  completeTask(fieldData, taskId, userId) {
    return this.httpClient.post("http://localhost:8080/rest-api/completeTask/" + taskId + "/" + userId, fieldData) as Observable<any>
  }

  getProcessVariable(processInstanceId, varName) {
    return this.httpClient.get("http://localhost:8080/rest-api/getVar/" + processInstanceId + "/" + varName) as Observable<any>
  }

  deleteUser(userId) {
    return this.httpClient.delete("http://localhost:8080/rest-api/deleteUser/" + userId) as Observable<any>
  }

  findMembership(userId) {
    return this.httpClient.get("http://localhost:8080/rest-api/membership/" + userId) as Observable<any>
  }

}
