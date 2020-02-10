import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../article.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-article-task',
  templateUrl: './article-task.component.html',
  styleUrls: ['./article-task.component.css']
})
export class ArticleTaskComponent implements OnInit {

  user = JSON.parse(localStorage.getItem("user"));
  userTasks = [];

  constructor(private articleService: ArticleService, private router: Router) {

  }

  ngOnInit() {
    this.articleService.getTasks(this.user.id).subscribe(
      tasks => {
        this.userTasks = tasks;
        console.log(tasks);
      },
      err => {
        console.log("error");
      }
    );
  }

  taskChosen(task) {
    console.log(task);
    localStorage.setItem("chosenTask", JSON.stringify(task));
    this.router.navigateByUrl("/article-task-chosen");
  }

}
