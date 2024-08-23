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

  isAuth: any;

  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    // this.authFailed = this.route.snapshot.paramMap.get('authFailed');
    // console.log(this.router.getCurrentNavigation().extras.state.example)
    // this.isAuth = localStorage.getItem('isAuth');
    // this.isAuth = JSON.parse(this.isAuth)
    // console.log(this.isAuth)

    this.authService.isAuth$.subscribe(status => {
      console.log(status)
      this.isAuth = status;
    });
  }

}
