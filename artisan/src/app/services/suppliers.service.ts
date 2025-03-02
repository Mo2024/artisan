import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SuppliersService {

  private apiUrl = environment.apiUrl;

  url = `${this.apiUrl}/api/accounts`;
  constructor(private http: HttpClient) { }

  addSupplier(name: string, description: string) {
    const body = { name, description };
    return this.http.post(`${this.url}/`, body, { withCredentials: true });
  }

  getSuppliers(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }
  deleteSupplier(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }
  editSupplier(id: number, name: string, description: string): Observable<any> {
    const body = { id, name, description };
    return this.http.put(`${this.url}/`, body, { withCredentials: true });
  }
}
