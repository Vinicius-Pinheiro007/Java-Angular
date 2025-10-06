import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Pedido {
  id: number;
  clienteId: number;
  valor: number;
  dataCriacao: string;
  itens?: Array<{ produtoId: number; quantidade: number; valorUnitario: number; numeroSerie?: string }>;
  status?: 'LANCADO' | 'EM_ANDAMENTO' | 'DESPACHADO';
}

export interface PedidoDTO {
  clienteId: number | null;
  valor: number | null;
  itens?: Array<{
    produtoId: number;
    quantidade: number;
    descricao?: string;
    valorUnitario: number;
    numeroSerie?: string;
  }>;
  status?: 'LANCADO' | 'EM_ANDAMENTO' | 'DESPACHADO';
}

@Injectable()
export class PedidosApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/pedidos`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(this.baseUrl);
  }

  buscar(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.baseUrl}/${id}`);
  }

  criar(dto: PedidoDTO): Observable<void> {
    return this.http.post<void>(this.baseUrl, dto);
  }

  atualizar(id: number, dto: PedidoDTO): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}`, dto);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}


