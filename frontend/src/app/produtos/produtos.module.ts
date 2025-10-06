import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { SharedModule } from '../shared/shared.module';

import { ProdutosListComponent } from './produtos-list/produtos-list.component';
import { ProdutosFormComponent } from './produtos-form/produtos-form.component';
import { ProdutosApiService } from './produtos-api.service';

const routes: Routes = [
  { path: 'produtos', component: ProdutosListComponent, data: { title: 'Produtos' } },
  { path: 'produtos/novo', component: ProdutosFormComponent, data: { title: 'Novo Produto' } },
  { path: 'produtos/editar/:id', component: ProdutosFormComponent, data: { title: 'Editar Produto' } }
];

@NgModule({
  declarations: [ProdutosListComponent, ProdutosFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatCardModule,
    MatIconModule,
    MatDialogModule,
    SharedModule,
    RouterModule.forChild(routes)
  ],
  providers: [ProdutosApiService]
})
export class ProdutosModule {}



