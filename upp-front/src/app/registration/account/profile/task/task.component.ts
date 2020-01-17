import { Component, OnInit } from '@angular/core';
import { RegistrationService } from 'src/app/registration/registration.service';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  private task = JSON.parse(localStorage.getItem("chosenTask"));
  private fields = [];

  constructor(private registrationService: RegistrationService) {
  }

  ngOnInit() {
  }

  onSubmit(value, form) {
    for (let v in value) {
      this.fields.push({ fieldId: v, fieldValue: value[v] });
    }
    this.registrationService.completeTask(this.fields, this.task.formdataDto.taskId, this.task.customUser.username).subscribe(
      res => {
        window.location.href = "/profile";
      },
      err => {
        console.log("Error occured while completing task.");
      }
    );
  }

}
