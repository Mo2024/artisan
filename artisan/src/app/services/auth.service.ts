import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { BehaviorSubject, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  url = 'http://localhost:3000/api/auth';

  private isAuthSubject = new BehaviorSubject<boolean>(false);
  isAuth$ = this.isAuthSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const storedAuth = localStorage.getItem('isAuth');
    this.isAuthSubject.next(storedAuth === 'true');
  }


  setAuth(status: boolean) {
    this.isAuthSubject.next(status);
    localStorage.setItem('isAuth', JSON.stringify(status));
  }

  login(username: string) {
    const body = { username };
    return this.http.post(`${this.url}/login`, body, { withCredentials: true })
  }
  register(username: string) {
    const body = { username };
    return this.http.post(`${this.url}/register`, body, { withCredentials: true })
  }
  check() {
    // const body = { username };
    return this.http.get(`${this.url}/isAuth`, { withCredentials: true })
  }

  // isAuth() {
  //   // console.log(await this.check())

  //   return this.http.get<any>(`${this.url}/isAuth`, { observe: 'response', withCredentials: true })
  //     .pipe(

  //       catchError(this.handleError.bind(this))
  //     );
  // }

  private handleError(error: HttpErrorResponse) {
    console.log(error)

    return throwError(error);
  }
}
