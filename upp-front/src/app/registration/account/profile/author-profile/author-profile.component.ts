import { Component, OnInit } from '@angular/core';
import { ArticleService } from 'src/app/article/article.service';

@Component({
  selector: 'app-author-profile',
  templateUrl: './author-profile.component.html',
  styleUrls: ['./author-profile.component.css']
})
export class AuthorProfileComponent implements OnInit {

  private user = JSON.parse(localStorage.getItem("user"));

  constructor(private articleService: ArticleService) { }

  ngOnInit() {
  }

  startProcessArticle() {
    this.articleService.startProcess(this.user.id).subscribe(
      res => {
        window.location.href = "/article-task";
      },
      err => {
        console.log("Error occurred while starting article process.");
      });
  }

}
