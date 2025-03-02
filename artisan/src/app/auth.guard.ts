import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, Observable, of } from 'rxjs';
import { AuthService } from './services/auth.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';


export const authGuard: CanActivateFn = (route, state) => {
  // const authService = inject(AuthService);
  const router = inject(Router);
  const http = inject(HttpClient)
  const authService = inject(AuthService)

  const apiUrl = environment.apiUrl;

  const url = `${apiUrl}/api/accounts`;
  return http.get<any>(`${url}/isAuth`, { withCredentials: true }).pipe(
    map((res: any) => {
      if (res.userId == null) {
        throw new Error("User is not authenticated");
      }
      return true; // If authenticated, allow access
    }),
    catchError((error) => {
      console.log(error)
      // authService.setAuth(false)
      router.navigate(['/auth/login']);
      return of(false);
    })
  );
};
