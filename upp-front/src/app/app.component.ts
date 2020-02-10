import { Component } from '@angular/core';
import { RegistrationService } from './registration/registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'upp-front';
  private user = null;

  constructor(private registrationService: RegistrationService, private router: Router) {
    this.user = JSON.parse(localStorage.getItem("user"));
  }

  logout() {
    this.registrationService.logout(localStorage.getItem("processInstanceId")).subscribe(
      res => {
        localStorage.removeItem("user");
        window.location.href = "login";
      }
    );
  }

  proba() {
    this.registrationService.proba().subscribe(
      success => {
        console.log("success");
      },
      err => {
        console.log("error");
      }
    );
  }
}
