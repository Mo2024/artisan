import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';

  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {

  }

  ngOnInit() {
    // console.log(this.route.snapshot.queryParamMap.get('authFailed'))

  }

  onSubmit() {
    if (!this.username.trim()) {
      alert('All fields must be filled out');
      return;
    }
    this.authService.login(this.username).subscribe({
      next: async (res) => {
        console.log(res)
        // const setItemPromise = new Promise<void>((resolve) => {
        //   localStorage.setItem('isAuth', JSON.stringify(true));
        //   resolve();
        // });

        // await setItemPromise
        // localStorage.setItem('isAuth', JSON.stringify(true));

        // Check if localStorage is updated
        // console.log('LocalStorage isAuth:', localStorage.getItem('isAuth'));

        // await localStorage.setItem('isAuth', JSON.stringify(true));
        this.authService.setAuth(true)
        this.router.navigate(['/sites']);
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
