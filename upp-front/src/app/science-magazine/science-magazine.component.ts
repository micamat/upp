import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ScienceMagazineService } from './science-magazine.service';

@Component({
  selector: 'app-science-magazine',
  templateUrl: './science-magazine.component.html',
  styleUrls: ['./science-magazine.component.css']
})
export class ScienceMagazineComponent implements OnInit {

  private formDto = null;
  private fields = [];
  private processInstanceId = "";
  private scienceFields = [];
  private chosenScienceFields = [];
  private selectedScienceField = "";
  private router: Router;
  private editor = JSON.parse(localStorage.getItem("user"));

  constructor(private magazineService: ScienceMagazineService, private _router: Router) {
    this.router = _router;
    magazineService.getMagazineFormData().subscribe(
      data => {
        console.log(data);
        this.formDto = data;
        this.fields = data.fields;
        this.processInstanceId = data.processInstanceId;
        this.fields.forEach(
          (field) => {
            if (field.type.name == 'enum') {
              this.scienceFields = Object.keys(field.type.values);
            }
          }
        );
      },
      err => {
        console.log("Error occured while fetching magazine form data.");
      }
    );
  }

  ngOnInit() {
  }

  onSubmit(value, form) {
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
    console.log(formFieldData);
    // submitting new magazine
    this.magazineService.addMagazine(formFieldData, this.formDto.taskId, this.formDto.processInstanceId, this.editor.id).subscribe(
      res => {
        this.router.navigateByUrl("/editor-profile");
      },
      err => {
        console.log("error");
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
