import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {
  private apiUrl = environment.apiUrl;

  url = `${this.apiUrl}/api/accounts`;

  constructor(private http: HttpClient) { }

  addAccount(name: string, balance: string) {
    const body = { name, balance };
    return this.http.post(`${this.url}/createAccount`, body, { withCredentials: true });
  }
  addBalance(addedBalance: string, accountId: number, description: string, date: String) {
    const body = { accountId, addedBalance, description, date };
    return this.http.put(`${this.url}/addBalance`, body, { withCredentials: true });
  }

  getAccounts(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }


}
