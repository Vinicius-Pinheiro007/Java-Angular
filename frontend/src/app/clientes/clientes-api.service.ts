import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Cliente { id: number; nome: string; documento?: string; }

@Injectable({ providedIn: 'root' })
export class ClientesApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/clientes`;
  constructor(private http: HttpClient) {}
  listar(): Observable<Cliente[]> { return this.http.get<Cliente[]>(this.baseUrl); }
}




