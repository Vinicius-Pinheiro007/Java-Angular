import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { PedidosApiService, Pedido } from '../pedidos-api.service';
import { ClientesApiService, Cliente } from '../../clientes/clientes-api.service';
import { SimpleConfirmDialog } from '../shared/simple-confirm.dialog';

@Component({
  selector: 'app-pedidos-list',
  templateUrl: './pedidos-list.component.html',
  styleUrls: ['./pedidos-list.component.css']
})
export class PedidosListComponent implements OnInit {
  displayedColumns = ['id', 'cliente', 'valor', 'status', 'dataCriacao', 'acoes'];
  dataSource: Pedido[] = [];
  loading = false;
  clienteNomeById = new Map<number,string>();

  constructor(
    private api: PedidosApiService,
    private clientesApi: ClientesApiService,
    private snack: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.loading = true;
    this.clientesApi.listar().subscribe({
      next: clientes => {
        clientes.forEach(c => this.clienteNomeById.set(c.id, c.nome));
        this.api.listar().subscribe({
          next: dados => { this.dataSource = dados; this.loading = false; },
          error: () => { this.snack.open('Erro ao carregar pedidos', 'Fechar', { duration: 3000 }); this.loading = false; }
        });
      },
      error: () => { this.api.listar().subscribe(d => { this.dataSource = d; this.loading = false; }); }
    });
  }

  novo(): void {
    this.router.navigate(['/pedidos/novo']);
  }

  confirmarExclusao(id: number): void {
    const ref = this.dialog.open(SimpleConfirmDialog, { data: { titulo: 'Confirmação', mensagem: 'Deseja excluir este pedido?' } });
    ref.afterClosed().subscribe(ok => {
      if (ok) {
        this.api.remover(id).subscribe({
          next: () => { this.snack.open('Pedido excluído', 'Fechar', { duration: 2000 }); this.carregar(); },
          error: () => this.snack.open('Erro ao excluir', 'Fechar', { duration: 3000 })
        });
      }
    });
  }

  editar(id: number): void {
    this.router.navigate(['/pedidos/editar', id]);
  }
}



