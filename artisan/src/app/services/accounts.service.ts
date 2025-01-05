import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {


  url = 'http://localhost:3000/api/accounts';

  constructor(private http: HttpClient) { }

  addAccount(name: string, balance: string) {
    const body = { name, balance };
    return this.http.post(`${this.url}/createAccount`, body, { withCredentials: true });
  }
  addBalance(addedBalance: string, accountId: number) {
    const body = { accountId, addedBalance };
    return this.http.put(`${this.url}/createAccount`, body, { withCredentials: true });
  }

  getAccounts(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }


}
