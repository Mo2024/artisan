import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CashService {

  url = 'http://localhost:3000/api/cash';

  constructor(private http: HttpClient) { }

  addCash(date: string, paidBy: string, paymentMethod: string, siteId: string, cost: string, description: string, accountId: any) {
    const body = { date, paidBy, paymentMethod, siteId, cost, description, accountId };
    return this.http.post(`${this.url}/`, body, { withCredentials: true });
  }

  getCash(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }

  editCash(id: number, date: string, paid_by: string, payment_method: string, site_id: string, cost: string, description: string, account_id: any): Observable<any> {
    const body = { id, date, paidBy: paid_by, paymentMethod: payment_method, siteId: site_id, cost, description, accountId: account_id };
    console.log(account_id)
    return this.http.put(`${this.url}/`, body, { withCredentials: true });
  }
  deleteCash(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }
}
