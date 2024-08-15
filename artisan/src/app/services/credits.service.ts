import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CreditsService {

  url = 'http://localhost:3000/api/credits';

  constructor(private http: HttpClient) { }

  addCredit(date: string, invoice_no: string, supplier_id: number, cost: string, description: string, site_id: number) {
    const body = { date, invoice_no, supplier_id, cost, description, site_id };
    return this.http.post(this.url, body, { withCredentials: true });
  }

  getCredits(): Observable<any> {
    return this.http.get(this.url, { withCredentials: true });
  }

  editCredit(id: number, date: string, invoice_no: string, supplier_id: number, cost: string, description: string, site_id: number): Observable<any> {
    const body = { id, date, invoice_no, supplier_id, cost, description, site_id };
    return this.http.put(`${this.url}/${id}`, body, { withCredentials: true });
  }
  deleteCredit(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }

}
