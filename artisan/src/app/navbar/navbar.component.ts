import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  isAuth: any = false;

  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    // this.authFailed = this.route.snapshot.paramMap.get('authFailed');
    // console.log(this.router.getCurrentNavigation().extras.state.example)
    // this.isAuth = localStorage.getItem('isAuth');
    // this.isAuth = JSON.parse(this.isAuth)
    // console.log(this.isAuth)

    // this.authService.isAuth().subscribe({
    //   next: (response) => {
    //     this.isAuth = !!response.userId; // Set to true if userId exists, false otherwise
    //   },
    //   error: (error) => {
    //     this.isAuth = false; // Handle errors by assuming not authenticated
    //   }
    // });

    // this.authService.isAuth$.subscribe({
    //   next: (authStatus) => {
    //     this.isAuth = authStatus; // Update the navbar based on auth status
    //   },
    //   error: (error) => {
    //     this.isAuth = false; // In case of error, assume not authenticated
    //   }
    // });

    this.authService.isAuth$.subscribe({
      next: (authStatus) => {
        this.isAuth = authStatus; // Update the navbar based on auth status
      },
      error: (error) => {
        this.isAuth = false; // In case of error, assume not authenticated
      }
    });

  }

}
