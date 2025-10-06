import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProdutosApiService, Produto } from '../produtos-api.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog.component';

@Component({
  selector: 'app-produtos-list',
  templateUrl: './produtos-list.component.html',
  styleUrls: ['./produtos-list.component.css']
})
export class ProdutosListComponent implements OnInit {
  displayedColumns = ['id', 'nome', 'preco', 'acoes'];
  dataSource: Produto[] = [];

  constructor(
    private api: ProdutosApiService,
    private snack: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.api.listar().subscribe({
      next: dados => this.dataSource = dados,
      error: () => this.snack.open('Erro ao carregar produtos', 'Fechar', { duration: 3000 })
    });
  }

  novo(): void { this.router.navigate(['/produtos/novo']); }
  editar(id: number): void { this.router.navigate(['/produtos/editar', id]); }
  excluir(id: number): void {
    const ref = this.dialog.open(ConfirmDialogComponent, { data: { titulo: 'Excluir', mensagem: 'Deseja excluir este produto?' } });
    ref.afterClosed().subscribe(ok => {
      if (!ok) return;
      this.api.remover(id).subscribe({
        next: () => { this.snack.open('Produto excluído', 'Fechar', { duration: 2000 }); this.carregar(); },
        error: (e) => this.snack.open(e?.error?.message || 'Produto está em uso e não pode ser excluído', 'Fechar', { duration: 4000 })
      });
    });
  }
}



