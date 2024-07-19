import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  url = 'http://localhost:3000/api/auth';

  constructor(private http: HttpClient) { }

  login(username: string) {
    const body = { username };
    return this.http.post(`${this.url}/login`, body)
  }
  register(username: string) {
    const body = { username };
    return this.http.post(`${this.url}/register`, body)
  }
}
