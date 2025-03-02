import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CashService {

  private apiUrl = environment.apiUrl;

  url = `${this.apiUrl}/api/accounts`;

  constructor(private http: HttpClient) { }

  addCash(date: string, paidBy: string, paymentMethod: string, siteId: string, cost: string, description: string, accountId: any, type: string) {
    const body = { date, paidBy, paymentMethod, siteId, cost, description, accountId, isCredit: false, type };
    return this.http.post(`${this.url}/`, body, { withCredentials: true });
  }

  getCash(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }

  getTypes(): Observable<any> {
    return this.http.get(`${this.url}/getTypes`, { withCredentials: true });
  }
  getTransactionsByAccountId(accountId: string): Observable<any> {
    return this.http.get(`${this.url}/getByAccountId/${accountId}`, { withCredentials: true });
  }

  deleteCash(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }
}
