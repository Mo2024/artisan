import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CreditsService {

  private apiUrl = environment.apiUrl;

  url = `${this.apiUrl}/api/accounts`;

  constructor(private http: HttpClient) { }

  addCredit(date: string, invoice_no: string, supplier_id: number, cost: string, description: string, site_id: number, type: string) {
    const body = { date, invoiceNo: invoice_no, creditorId: supplier_id, cost, description, siteId: site_id, type };
    console.log(body)
    return this.http.post(`${this.url}/`, body, { withCredentials: true });
  }

  getCredits(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }

  deleteCredit(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }

  payCredit(date: string, siteId: string, cost: string, description: string, accountId: any, creditId: any) {
    const body = { date, siteId, cost, description, accountId, isCredit: true, creditId };
    return this.http.put(`${this.url}/payCreditor`, body, { withCredentials: true });
  }

}
