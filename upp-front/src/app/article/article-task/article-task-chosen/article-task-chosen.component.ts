import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../article.service';

@Component({
  selector: 'app-article-task-chosen',
  templateUrl: './article-task-chosen.component.html',
  styleUrls: ['./article-task-chosen.component.css']
})
export class ArticleTaskChosenComponent implements OnInit {

  chosenTask = JSON.parse(localStorage.getItem("chosenTask"));
  fields = [];
  enumValues = [];
  selected;

  constructor(private articleService: ArticleService) { }

  ngOnInit() {
    this.articleService.getFormData(this.chosenTask.id).subscribe(
      formData => {
        this.fields = formData;
        this.fields.forEach(
          (field) => {
            if (field.type.name == 'enum') {
              this.enumValues = Object.keys(field.type.values);
            }
          }
        );
        console.log(formData);
      },
      err => {
        console.log("error form data");
      }
    );
  }

  onSubmit(value, form) {
    console.log(value);
    let formFields = new Array();
    for (let v in value) {
      formFields.push({ fieldId: v, fieldValue: value[v] });
    }
    this.articleService.completeTask(formFields, this.chosenTask.id).subscribe(
      res => {
        console.log("task completed");
        //window.location.href = "/article-task"
      },
      err => {
        console.log("error while completing task.")
      }
    );
  }

}
