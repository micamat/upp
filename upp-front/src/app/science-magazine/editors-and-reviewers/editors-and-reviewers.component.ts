import { Component, OnInit } from '@angular/core';
import { ScienceMagazineService } from '../science-magazine.service';
import { Magazine } from '../magazine.model';

@Component({
  selector: 'app-editors-and-reviewers',
  templateUrl: './editors-and-reviewers.component.html',
  styleUrls: ['./editors-and-reviewers.component.css']
})
export class EditorsAndReviewersComponent implements OnInit {

  private selectedEditor = "";
  private selectedReviewer = "";
  private chosenEditors = [];
  private chosenReviewers = [];
  private task = JSON.parse(localStorage.getItem("chosenTask"));
  private editors = [];
  private reviewers = [];
  private magazineScienceFields = [];
  private fields = [];
  private updatedMagazine = null;

  constructor(private magazineService: ScienceMagazineService) {
    this.updatedMagazine = new Magazine(this.task.magazine.id, this.task.magazine.name, this.task.magazine.issn, this.task.magazine.isOpenAcces, this.task.magazine.editors, this.task.magazine.reviewers, this.task.magazine.active);
    if (this.task.magazine != null) {
      magazineService.findEditors(this.task.magazine.scienceFields).subscribe(
        editors => {
          console.log(editors);
          this.editors = editors;
        },
        err => {
          console.log("Error occurred while fetching editors.");
        }
      );
      magazineService.findReviewers(this.task.magazine.scienceFields).subscribe(
        revs => {
          console.log(revs);
          this.reviewers = revs;
        },
        err => {
          console.log("Error occurred while fetching reviewers");
        }
      );
    }
  }

  ngOnInit() {
  }

  onSubmit(value, form) {
    if (this.task.name == "Add editors and reviewers") {
      let formFieldData = new Array();
      let temp = "";
      let temp1 = "";
      for (let editor in this.chosenEditors) {
        if (temp == "") {
          temp = this.chosenEditors[editor].username;
        }
        else {
          temp = temp + "," + this.chosenEditors[editor].username;
        }
      }
      formFieldData.push({ fieldId: "editors", fieldValue: temp });

      for (let rev in this.chosenReviewers) {
        if (temp1 == "") {
          temp1 = this.chosenReviewers[rev].username;
        }
        else {
          temp1 = temp1 + "," + this.chosenReviewers[rev].username;
        }
      }
      formFieldData.push({ fieldId: "reviewers", fieldValue: temp1 });
      this.magazineService.completeTaskAddEditorsAndReviewers(formFieldData, this.task.formdataDto.taskId, this.task.magazine.id).subscribe(
        success => {
          window.location.href = "/editor-profile";
        },
        err => {
          console.log("Error occurred while completing task.");
        }
      );
    }
    else if (this.task.name == "Check magazine data") {
      for (let v in value) {
        this.fields.push({ fieldId: v, fieldValue: value[v] });
      }
      this.magazineService.completeTaskAddEditorsAndReviewers(this.fields, this.task.formdataDto.taskId, this.task.magazine.id).subscribe(
        success => {
          window.location.href = "/profile";
        },
        err => {
          console.log("Error occurred while activating magazine.");
        }
      );
    }
    else if (this.task.name == "Correct data") {
      console.log(this.updatedMagazine);
      this.magazineService.updateMagazine(this.updatedMagazine).subscribe(
        success => {
          this.magazineService.completeTaskAddEditorsAndReviewers([], this.task.formdataDto.taskId, this.task.magazine.id).subscribe(
            res => {
              window.location.href = "/profile";
            },
            err => {
              console.log("Error occurred while completing task");
            }
          );
        },
        error => {
          console.log("Error occurred while updating magazine data");
        }
      );
    }
  }

  onAddEditor() {
    for (let editor in this.editors) {
      if (this.editors[editor].username == this.selectedEditor) {
        if (!this.chosenEditors.find(x => x.username == this.selectedEditor)) {
          this.chosenEditors.push(this.editors[editor]);
        }
        else {
          alert("You have already added this editor.");
        }
      }
    }
  }

  onAddReviewer() {
    for (let rev in this.reviewers) {
      if (this.reviewers[rev].username == this.selectedReviewer) {
        if (!this.chosenReviewers.find(x => x.username == this.selectedReviewer)) {
          this.chosenReviewers.push(this.reviewers[rev]);
        }
        else {
          alert("You have already added this reviewer.");
        }
      }
    }
  }

}
