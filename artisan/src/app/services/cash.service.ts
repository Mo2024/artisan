import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CashService {

  url = 'http://localhost:3000/api/cash';

  constructor(private http: HttpClient) { }

  addCash(date: string, paid_by: string, payment_method: string, site_id: string, cost: string, description: string) {
    const body = { date, paid_by, payment_method, site_id, cost, description };
    return this.http.post(this.url, body);
  }

  getCash(): Observable<any> {
    return this.http.get(this.url);
  }

  editCash(id: number, date: string, paid_by: string, payment_method: string, site_id: string, cost: string, description: string): Observable<any> {
    const body = { date, paid_by, payment_method, site_id, cost, description };
    return this.http.put(`${this.url}/${id}`, body);
  }
  deleteCash(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`);
  }
}
