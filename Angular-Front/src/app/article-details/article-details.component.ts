import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { Article } from '../models/article.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from '../_services/article.service';
import { StorageService } from '../_services/storage.service';
import { Comment } from '../models/comment.model';
import { CommentService } from '../_services/comment.service';

@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.css']
})
export class ArticleDetailsComponent implements OnInit {

  private roles: string[] = [];
  isLoggedIn = false;
  isAdmin = false;
  isModerator = false;
  isModOrAdmin = false;
  whatHappened?: string
  
  comments: any

  comment: Comment = {
    article_id: null,
    text: '',
    user_id: null
  }
  submitted = false;

  @Input() viewMode = false;
  @Input() currentArticle: Article = {
    id: '',
    title: '',
    description: '',
    published: false
  };
  @Input() currentArticleID?: number;
  
  message = ''

  constructor(private commentService: CommentService,private articleService: ArticleService, private storageService: StorageService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.whatHappened = ''
    if (!this.viewMode) {
      this.message = '';
      this.getArticle(this.route.snapshot.params["id"])
    }
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isModerator = this.roles.includes('ROLE_MODERATOR');
      if (this.roles.includes('ROLE_ADMIN') || this.roles.includes('ROLE_MODERATOR')){
        this.isModOrAdmin = true;
      }
    }
  }
  ngOnChanges(): void {
    console.log(this.currentArticleID)
    this.getComments(this.currentArticleID)
    this.whatHappened = ''
    
  }

  getArticle(id: string): void {
    this.articleService.get(id)
      .subscribe({
        next: (data) => {
          this.currentArticle = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
  updatePublished(status: boolean): void {
    const data = {
      title: this.currentArticle.title,
      description: this.currentArticle.description,
      published: status
    };
    this.message = '';
    this.articleService.update(this.currentArticle.id, data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.currentArticle.published = status;
          this.message = res.message ? res.message : 'The status was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }
  updateArticle(): void {
    this.message = '';
    this.articleService.update(this.currentArticle.id, this.currentArticle)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.message = res.message ? res.message : 'This article was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }
  deleteArticle(): void {
    this.articleService.delete(this.currentArticle.id)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.router.navigate(['/articles']);
        },
        error: (e) => console.error(e)
      });
  }

  deleteComment(id:any):void {
    this.commentService.deleteById(id).subscribe({
      next: (res) => {
        console.log(res)
        this.whatHappened = 'Deleted successfully! Reload page to see!'
      },
      error: (e) => console.error(e)      
    })
  }

  retrieveComments(): void {
    this.commentService.getAll().subscribe({
      next: (data) => {
        this.comments = data;
        console.log(data);
      },
      error: (e) => console.error(e)
    })
  }
  getComments(id: any): void {
    this.commentService.getByArticle(id)
      .subscribe({
        next: (data) => {
          this.comments = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
  saveComment(): void {
    const data = {
      article_id: this.currentArticle.id,
      text: this.comment.text,
      user_id: this.storageService.getUserId()
    };
    console.log(data)
    this.commentService.create(data).subscribe({
      next: (res) => {
        console.log(res);

        this.comment.text=''
        
        this.whatHappened = 'Comment left successfully! Reload page to see!'
        
      },
      error: (e) => console.error(e)
    });
  }

}
