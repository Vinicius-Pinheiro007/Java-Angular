import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClientesApiService, Cliente } from '../clientes-api.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit {
  form = this.fb.group({
    id: [undefined as number | undefined],
    nome: ['', [Validators.required]],
    documento: ['', [Validators.required]]
  });

  constructor(
    private fb: FormBuilder,
    private api: ClientesApiService,
    private snack: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.http.get<Cliente>(`${location.origin.replace('4200','8080')}/clientes/${id}`).subscribe(c => this.form.patchValue(c));
    }
  }

  salvar(): void {
    if (this.form.invalid) { this.snack.open('Informe o nome', 'Fechar', { duration: 3000 }); return; }
    const c = this.form.value as Cliente;
    if (c.id) {
      this.http.put(`${location.origin.replace('4200','8080')}/clientes/${c.id}`, c).subscribe({
        next: () => { this.snack.open('Cliente salvo', 'Fechar', { duration: 2000 }); this.router.navigate(['/clientes']); },
        error: () => this.snack.open('Erro ao salvar', 'Fechar', { duration: 3000 })
      });
    } else {
      this.http.post(`${location.origin.replace('4200','8080')}/clientes`, c).subscribe({
        next: () => { this.snack.open('Cliente salvo', 'Fechar', { duration: 2000 }); this.router.navigate(['/clientes']); },
        error: () => this.snack.open('Erro ao salvar', 'Fechar', { duration: 3000 })
      });
    }
  }

  maskDocumento(event: any): void {
    const input = event.target as HTMLInputElement;
    const digits = (input.value || '').replace(/\D+/g, '').slice(0, 14);
    let masked = digits;
    if (digits.length <= 11) {
      // CPF: 000.000.000-00
      masked = digits
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    } else {
      // CNPJ: 00.000.000/0000-00
      masked = digits
        .replace(/(\d{2})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1/$2')
        .replace(/(\d{4})(\d{1,2})$/, '$1-$2');
    }
    input.value = masked;
    this.form.patchValue({ documento: masked });
  }
}




