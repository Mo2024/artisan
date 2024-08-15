import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';

  constructor(private authService: AuthService) { }

  onSubmit() {
    if (!this.username.trim()) {
      alert('All fields must be filled out');
      return;
    }
    this.authService.login(this.username).subscribe({
      next: (res) => {
        console.log(res)
      },
      error: (error) => {
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
