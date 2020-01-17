import { Component, OnInit } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { RegistrationService } from '../../registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  userTasks = [];
  user = JSON.parse(localStorage.getItem("user"));
  _router: Router;

  constructor(private registrationService: RegistrationService, private router: Router) {
    this._router = router;
    registrationService.getTasks(this.user.id).subscribe(
      tasks => {
        tasks.forEach(element => {
          this.userTasks.push(element);
        });
      },
      err => {
        console.log("Error occured while fetching tasks.");
      }
    );
  }

  ngOnInit() {
  }

  taskChosen(task) {
    localStorage.setItem("chosenTask", JSON.stringify(task));
    if (task.customUser != null) {
      this._router.navigateByUrl("/task");
    }
    else if (task.magazine != null) {
      this.router.navigateByUrl("/editors-and-reviewers");
    }
  }

}
