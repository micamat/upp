import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mail',
  templateUrl: './mail.component.html',
  styleUrls: ['./mail.component.css']
})
export class MailComponent implements OnInit {

  private router: Router;

  constructor(private registrationService: RegistrationService, private _router: Router) {
    this.router = _router;
  }

  ngOnInit() {
  }

  requestMail() {
    let req = this.registrationService.mailRequested(localStorage.getItem("processInstanceId"));
    req.subscribe(
      res => {
        this.router.navigateByUrl("/mail-sent");
      },
      err => {
        console.log("error");
      }
    );
  }

}
