import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent {


  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {
    this.logout()
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        // Update authentication state
        this.authService.setAuth(false);
        // Redirect to login page
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        console.error('Logout error:', error);
        // Optionally handle logout error
      }
    });
  }

}
