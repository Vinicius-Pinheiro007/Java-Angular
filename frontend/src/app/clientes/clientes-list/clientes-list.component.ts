import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClientesApiService, Cliente } from '../clientes-api.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog.component';

@Component({
  selector: 'app-clientes-list',
  templateUrl: './clientes-list.component.html',
  styleUrls: ['./clientes-list.component.css']
})
export class ClientesListComponent implements OnInit {
  displayedColumns = ['id', 'nome', 'documento', 'acoes'];
  dataSource: Cliente[] = [];
  filterValue = '';

  constructor(
    private api: ClientesApiService,
    private snack: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.api.listar().subscribe({
      next: dados => this.dataSource = dados,
      error: () => this.snack.open('Erro ao carregar clientes', 'Fechar', { duration: 3000 })
    });
  }

  filteredData(): Cliente[] {
    const term = (this.filterValue || '').toLowerCase();
    if (!term) return this.dataSource;
    return this.dataSource.filter(c => (c.nome || '').toLowerCase().includes(term) || (c.documento || '').toLowerCase().includes(term));
  }

  novo(): void { this.router.navigate(['/clientes/novo']); }
  editar(id: number): void { this.router.navigate(['/clientes/editar', id]); }
  excluir(id: number): void {
    const ref = this.dialog.open(ConfirmDialogComponent, { data: { titulo: 'Excluir', mensagem: 'Deseja excluir este cliente?' } });
    ref.afterClosed().subscribe(ok => {
      if (!ok) return;
      fetch(`${location.origin.replace('4200','8080')}/clientes/${id}`, { method: 'DELETE' }).then(r => {
        if (r.status === 204) { this.snack.open('Cliente excluído', 'Fechar', { duration: 2000 }); this.carregar(); return; }
        return r.json().then(j => { throw new Error(j?.message || 'Cliente possui pedidos e não pode ser excluído'); });
      }).catch(err => this.snack.open(err.message, 'Fechar', { duration: 4000 }));
    });
  }
}



