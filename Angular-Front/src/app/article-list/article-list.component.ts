import { Component, OnInit } from '@angular/core';
import { Article } from '../models/article.model';
import { ArticleService } from '../_services/article.service';
import { StorageService } from '../_services/storage.service';
import { Comment } from '../models/comment.model';
import { CommentService } from '../_services/comment.service';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit {

  private roles: string[] = [];
  isLoggedIn = false;
  isAdmin = false;
  isModerator = false;
  isModOrAdmin = false;

  articleId?:number
  textCom?:string
  userId?:number

  comments: Comment[] = []

  articles?: Article[];
  currentArticle: Article = {}
  currentIndex = -1
  title = ''
  currentArticleID: number = 0

  private comment: Comment = {
    article_id: null,
    text: '',
    user_id: null
  }
  submitted = false;

  constructor(private articleService: ArticleService, private storageService: StorageService, private commentService: CommentService) { }

  ngOnInit(): void {
    // this.retrieveArticles()
    // this.retrieveComments()

    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isModerator = this.roles.includes('ROLE_MODERATOR');
      if (this.roles.includes('ROLE_ADMIN') || this.roles.includes('ROLE_MODERATOR')){
        this.isModOrAdmin = true;
      }

      if (this.isModOrAdmin) {
        this.retrieveAllArticles()
      } else {
        this.retrieveArticles()
      }

    }

  }
  retrieveComments(): void {
    this.commentService.getAll().subscribe({
      next: (data) => {
        this.comments = data;
        console.log(data);
        console.log(this.comments)
      },
      error: (e) => console.error(e)
    })
  }
  getComment(id: number): void {
    this.commentService.getByArticle(id)
      .subscribe({
        next: (data) => {

          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
  
  retrieveArticles(): void {
    this.articleService.getPublished()
      .subscribe({
        next: (data) => {
          this.articles = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  retrieveAllArticles(): void {
    this.articleService.getAll()
      .subscribe({
        next: (data) => {
          this.articles = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  refreshList(): void {
    this.retrieveArticles();
    this.currentArticle = {};
    this.currentIndex = -1;
  }
  setActiveArticle(article: Article, index: number): void {
    this.currentArticle = article;

    this.currentIndex = index;
    this.currentArticleID = this.currentArticle.id
  }
  removeAllArticles(): void {
    this.articleService.deleteAll()
      .subscribe({
        next: (res) => {
          console.log(res);
          this.refreshList();
        },
        error: (e) => console.error(e)
      });
  }
  searchTitle(): void {
    this.currentArticle = {};
    this.currentIndex = -1;
    this.articleService.findByTitle(this.title)
      .subscribe({
        next: (data) => {
          this.articles = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
  saveComment(): void {
    const data = {
      article_id: this.comment.article_id,
      text: this.comment.text,
      user_id: this.comment.user_id
    };
    this.commentService.create(data).subscribe({
      next: (res) => {
        console.log(res);
        this.submitted=true;
      },
      error: (e) => console.error(e)
    });
  }

}
