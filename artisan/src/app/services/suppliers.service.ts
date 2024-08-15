import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SuppliersService {

  url = 'http://localhost:3000/api/suppliers';

  constructor(private http: HttpClient) { }

  addSupplier(name: string, description: string) {
    const body = { name, description };
    return this.http.post(this.url, body, { withCredentials: true });
  }

  getSuppliers(): Observable<any> {
    return this.http.get(this.url, { withCredentials: true });
  }
  deleteSupplier(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }
  editSupplier(id: number, name: string, description: string): Observable<any> {
    const body = { name, description };
    return this.http.put(`${this.url}/${id}`, body, { withCredentials: true });
  }
}
