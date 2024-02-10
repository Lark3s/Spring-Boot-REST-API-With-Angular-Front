import { Component } from '@angular/core';
import { StorageService } from './_services/storage.service';
import { AuthService } from './_services/auth.service';
import { Router } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { NewsletterService } from './_services/newsletter.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private roles: string[] = [];
  isLoggedIn = false;
  isAdmin = false;
  isModerator = false;
  isModOrAdmin = false;
  username?: string;

  newsletterMail?: string;

  constructor(private storageService: StorageService, private authService: AuthService, private newsletterService: NewsletterService, private router:Router) { }

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.isAdmin = this.roles.includes('ROLE_ADMIN');
      this.isModerator = this.roles.includes('ROLE_MODERATOR');
      if (this.roles.includes('ROLE_ADMIN') || this.roles.includes('ROLE_MODERATOR')){
        this.isModOrAdmin = true;
      }

      this.username = user.username;
    }
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
        this.storageService.clean();
      },
      error: err => {
        console.log(err);
      }
    });
    
    window.location.reload();
    this.router.navigateByUrl('/home')
  }

  newsletterSignup(): void {
    const data = {
      email: this.newsletterMail
    }
    console.log(data)
    this.newsletterService.create(data).subscribe({
      next: (res) => {
        console.log(res)
        this.newsletterMail = ''
      }
    })
  }
}
