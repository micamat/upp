import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../../registration.service';
import { UserCredentials } from './user-credentials.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private userCredentials = new UserCredentials();
  private router: Router;

  constructor(private registrationService: RegistrationService, _router: Router) {
    this.router = _router;
  }

  ngOnInit() {
  }

  onSubmit(value, form) {
    for (let v in value) {
      if (v == "username") {
        this.userCredentials.password = value[v];
      }
      if (v == "password") {
        this.userCredentials.authenticatedUserPassword = value[v];
      }
    }
    /* visekorisnicki rezim */
    let tempInstanceId = localStorage.getItem("processInstanceId");
    if (tempInstanceId == null) {
      tempInstanceId = localStorage.getItem("adminProcessInstanceId");
    }
    /*--------------------- */
    let log = this.registrationService.login(tempInstanceId, this.userCredentials);
    log.subscribe(
      loggedUser => {
        if (loggedUser != null) {
          localStorage.setItem("user", JSON.stringify(loggedUser));
          this.registrationService.findInstanceIdByUser(loggedUser.id).subscribe(
            instance => {
              console.log("instance: " + instance);
              localStorage.setItem("processInstanceId", instance);
              this.registrationService.findMembership(loggedUser.id).subscribe(
                res => {
                  //if (res == "editor") {
                  //window.location.href = "/editor-profile";
                  //}
                  //else if (res == "author") {
                  window.location.href = "/article-task";
                  //}
                  //else {
                  //window.location.href = "/profile";
                  //}
                }
              );
            },
            err => {
              console.log("Error while fetching instance id.");
            }
          );
        }
        else {
          alert("Bad credentials!");
        }
      },
      err => {
        console.log("Error occured while submitting credentials.");
      }
    );
  }

}
