import { Component, OnInit } from '@angular/core';
import { RegistrationService } from './registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  private formDto = null;
  private fields = [];
  private processInstanceId = "";
  private scienceFields = [];
  private chosenScienceFields = [];
  private selectedScienceField = "";
  private router: Router;
  private validatedUser = JSON.parse(localStorage.getItem("validatedUser"));

  constructor(private registrationService: RegistrationService, private _router: Router) {
    this.router = _router;
    if (this.validatedUser == null) {
      this.validatedUser = {};
    }
    let formData = registrationService.getFormData();
    formData.subscribe(
      data => {
        console.log(data);
        this.formDto = data;
        this.fields = data.fields;
        this.processInstanceId = data.processInstanceId;
        localStorage.setItem("adminProcessInstanceId", this.processInstanceId);
        this.fields.forEach(
          (field) => {
            if (field.type.name == 'enum') {
              this.scienceFields = Object.keys(field.type.values);
            }
          }
        );
      },
      err => {
        console.log("Error occured while fetching form data.");
      }
    );
  }

  ngOnInit() {
  }

  onSubmit(value, form) {
    for (let c in form.controls) {
      console.log(c + " =====>> " + form.controls[c].value);
    }
    let formFieldData = new Array();
    localStorage.setItem("processInstanceId", this.processInstanceId);
    for (let v in value) {
      if (v == "scienceFields") {
        let temp = "";
        this.chosenScienceFields.forEach(
          x => {
            if (temp == "") {
              temp = temp + x;
            }
            else {
              temp = temp + "," + x;
            }
          }
        );
        formFieldData.push({ fieldId: v, fieldValue: temp });
      }
      else {
        formFieldData.push({ fieldId: v, fieldValue: value[v] });
      }
    }
    let saving = this.registrationService.apply(formFieldData, this.formDto.taskId, this.formDto.processInstanceId);
    saving.subscribe(
      res => {
        this.registrationService.getProcessVariable(this.processInstanceId, "validatedUser").subscribe(
          result => {
            if (result != null) {
              localStorage.setItem("validatedUser", JSON.stringify(result));
              console.log("poruka: " + result.confirmPassword);
              if (result.confirmPassword == "Password confirmation failed.") {
                alert("Data is not valid! " + result.confirmPassword);
              }
              else {
                alert("Data is not valid!");
              }
              console.log("res: " + res);
              for (let r in res) {
                console.log("res[" + r + "]: " + res[r]);
              }
              this.registrationService.deleteUser(res.username).subscribe(
                deleted => {
                  console.log("User deleted");
                  window.location.href = "registrate";
                }
              );
            }
            else {
              this.router.navigateByUrl('/mail-sent');
            }
          }
        );
      },
      err => {
        console.log("Error");
      }
    );
  }

  onAdd() {
    this.fields.forEach(
      x => {
        if (x.type.name == "enum") {
          if (!this.chosenScienceFields.find(y => y == x.type.values[this.selectedScienceField])) {
            this.chosenScienceFields.push(x.type.values[this.selectedScienceField]);
          }
          else {
            alert("You have already chosen this science field.");
          }
        }
      }
    );
  }

}
