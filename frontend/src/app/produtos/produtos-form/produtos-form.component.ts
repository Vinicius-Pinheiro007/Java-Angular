import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProdutosApiService, Produto } from '../produtos-api.service';

@Component({
  selector: 'app-produtos-form',
  templateUrl: './produtos-form.component.html',
  styleUrls: ['./produtos-form.component.css']
})
export class ProdutosFormComponent implements OnInit {
  form = this.fb.group({
    id: [undefined as number | undefined],
    nome: ['', [Validators.required]],
    preco: [null as number | null, [Validators.required, Validators.min(0.01)]]
  });

  constructor(
    private fb: FormBuilder,
    private api: ProdutosApiService,
    private snack: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.api.buscar(+id).subscribe(prod => this.form.patchValue(prod));
    }
  }

  salvar(): void {
    if (this.form.invalid) {
      this.snack.open('Preencha os campos corretamente', 'Fechar', { duration: 3000 });
      return;
    }
    const produto = this.form.value as Produto;
    this.api.salvar(produto).subscribe({
      next: () => { this.snack.open('Produto salvo', 'Fechar', { duration: 2000 }); this.router.navigate(['/produtos']); },
      error: (e) => this.snack.open(e?.error?.message || 'Erro ao salvar', 'Fechar', { duration: 4000 })
    });
  }
}




