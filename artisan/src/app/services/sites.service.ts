import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SitesService {

  url = 'http://localhost:3000/api/sites';

  constructor(private http: HttpClient) { }

  addSite(name: string, description: string) {
    const body = { name, description };
    return this.http.post(`${this.url}/`, body, { withCredentials: true });
  }

  getSites(): Observable<any> {
    return this.http.get(`${this.url}/`, { withCredentials: true });
  }
  deleteSite(id: number): Observable<any> {
    return this.http.delete(`${this.url}/${id}`, { withCredentials: true });
  }
  editSite(id: number, name: string, description: string): Observable<any> {
    const body = { id, name, description };
    return this.http.put(`${this.url}/`, body, { withCredentials: true });
  }
}
