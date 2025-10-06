import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Produto {
  id?: number;
  nome: string;
  preco: number;
}

@Injectable()
export class ProdutosApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/produtos`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Produto[]> {
    return this.http.get<Produto[]>(this.baseUrl);
  }

  buscar(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.baseUrl}/${id}`);
  }

  salvar(produto: Produto): Observable<Produto> {
    if (produto.id) {
      return this.http.put<Produto>(`${this.baseUrl}/${produto.id}`, produto);
    }
    return this.http.post<Produto>(this.baseUrl, produto);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}




