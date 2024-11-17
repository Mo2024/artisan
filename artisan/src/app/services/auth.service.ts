import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, tap } from 'rxjs/operators';
import { BehaviorSubject, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  url = 'http://localhost:3000/api/auth';

  private isAuthSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false); // Initial value is false
  isAuth$: Observable<boolean> = this.isAuthSubject.asObservable(); // Expose the BehaviorSubject as an Observable

  // private isAuthSubject = new BehaviorSubject<boolean>(false);
  // isAuth$ = this.isAuthSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // const storedAuth = localStorage.getItem('isAuth');
    // this.isAuthSubject.next(storedAuth === 'true');
  }


  // setAuth(status: boolean) {
  //   this.isAuthSubject.next(status);
  //   localStorage.setItem('isAuth', JSON.stringify(status));
  // }

  // login(username: string) {
  //   const body = { username };
  //   return this.http.post(`${this.url}/login`, body, { withCredentials: true })
  // }

  login(username: string): Observable<any> {
    return this.http.post(`${this.url}/login`, { username }, { withCredentials: true }).pipe(
      tap((response: any) => {
        if (response.userId) {
          this.isAuthSubject.next(true); // Set to true when user is logged in
        }
      })
    );
  }


  logout() {
    return this.http.post(`${this.url}/logout`, {}, { withCredentials: true })
  }
  register(username: string) {
    const body = { username };
    return this.http.post(`${this.url}/register`, body, { withCredentials: true })
  }
  // check() {
  //   // const body = { username };
  //   return this.http.get<{ userId: number }>(`${this.url}/isAuth`, { withCredentials: true });
  // }

  isAuth(): Observable<any> {
    return this.http.get<{ userId: number }>(`${this.url}/isAuth`, { withCredentials: true });

  }

  // isAuth() {
  //   return this.http.get<{ userId: number }>(`${this.url}/isAuth`, { withCredentials: true });

  // }

  private handleError(error: HttpErrorResponse) {
    console.log(error)

    return throwError(error);
  }
}
