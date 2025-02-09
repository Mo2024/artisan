import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  username: string = '';
  isDisabled: boolean = false;

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit() {
    if (!this.username.trim()) {
      alert('All fields must be filled out');
      return;
    }

    this.isDisabled = true;
    this.authService.register(this.username).subscribe({
      next: (res) => {
        this.isDisabled = false;
        // this.authService.setAuth(true)
        this.router.navigate(['/sites']);
      },
      error: (error) => {
        this.isDisabled = false
        if (error.error.error) {
          alert(error.error.error)
        } else {
          console.error(error)
          alert('unknown error occured')
        }
      }
    })
  }

}
