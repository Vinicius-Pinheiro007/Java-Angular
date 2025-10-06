import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PedidosApiService } from '../pedidos-api.service';
import { ProdutosApiService, Produto } from '../../produtos/produtos-api.service';
import { ClientesApiService, Cliente } from '../../clientes/clientes-api.service';

@Component({
  selector: 'app-pedidos-form',
  templateUrl: './pedidos-form.component.html',
  styleUrls: ['./pedidos-form.component.css']
})
export class PedidosFormComponent implements OnInit {
  form = this.fb.group({
    clienteId: [null as number | null, [Validators.required]],
    status: ['LANCADO' as 'LANCADO' | 'EM_ANDAMENTO' | 'DESPACHADO', [Validators.required]],
    itens: this.fb.array([] as any[])
  });

  saving = false;
  produtos: Produto[] = [];
  clientes: Cliente[] = [];

  constructor(
    private fb: FormBuilder,
    private api: PedidosApiService,
    private produtosApi: ProdutosApiService,
    private clientesApi: ClientesApiService,
    private snack: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.produtosApi.listar().subscribe(ps => this.produtos = ps);
    this.clientesApi.listar().subscribe(cs => this.clientes = cs);
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.api.buscar(+id).subscribe(p => {
        this.form.patchValue({ clienteId: p.clienteId, status: (p.status as any) || 'LANCADO' });
        this.itens.clear();
        (p.itens || []).forEach((it: any) => this.itens.push(this.fb.group({
          produtoId: [ (it.produtoId ?? it.produto?.id ?? null), Validators.required],
          quantidade: [it.quantidade, [Validators.required, Validators.min(1)]],
          valorUnitario: [it.valorUnitario, [Validators.required, Validators.min(0.01)]],
          numeroSerie: [it.numeroSerie || '']
        })));
      });
    }
  }

  get itens(): FormArray {
    return this.form.get('itens') as FormArray;
  }

  addItem(): void {
    this.itens.push(this.fb.group({
      produtoId: [null, Validators.required],
      quantidade: [1, [Validators.required, Validators.min(1)]],
      valorUnitario: [null, [Validators.required, Validators.min(0.01)]],
      numeroSerie: ['']
    }));
  }

  removeItem(index: number): void {
    this.itens.removeAt(index);
  }

  get total(): number {
    return (this.itens.controls as any[]).reduce((acc: number, ctrl: any) => {
      const q = Number(ctrl.value?.quantidade) || 0;
      const vu = Number(ctrl.value?.valorUnitario) || 0;
      return acc + q * vu;
    }, 0);
  }

  onProdutoSelected(index: number, produtoId: number): void {
    const p = this.produtos.find(pp => pp.id === produtoId);
    const vu = p ? (p.preco as any) : null;
    this.itens.at(index).patchValue({ produtoId, valorUnitario: vu });
  }

  salvar(): void {
    if (this.form.invalid) {
      this.snack.open('Preencha os campos corretamente', 'Fechar', { duration: 3000 });
      return;
    }
    this.saving = true;
    const id = this.route.snapshot.paramMap.get('id');
    const payload = this.form.value as any;
    const obs = id ? this.api.atualizar(+id, payload) : this.api.criar(payload);
    obs.subscribe({
      next: () => {
        this.snack.open('Pedido criado com sucesso', 'Fechar', { duration: 2000 });
        this.router.navigate(['/pedidos']);
      },
      error: (err) => {
        const msg = err?.error?.message || 'Erro ao criar pedido';
        this.snack.open(msg, 'Fechar', { duration: 4000 });
        this.saving = false;
      }
    });
  }
}



