import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

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
        console.error(error)
      }
    })
  }

}
