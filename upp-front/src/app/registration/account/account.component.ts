import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../registration.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  private router: Router;

  constructor(private httpClient: HttpClient, private registrationService: RegistrationService, private _router: Router) {
    this.router = _router;
  }

  ngOnInit() {
  }

  mailConfirmed() {
    let temp = this.registrationService.mailConfirmed(localStorage.getItem("processInstanceId"));
    temp.subscribe(
      data => {
        localStorage.setItem("user", JSON.stringify(data));
        window.location.href = "/profile";
      },
      err => {
        console.log("Email confirmation error.");
      }
    );
  }

}
