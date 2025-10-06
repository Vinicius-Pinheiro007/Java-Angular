import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FilterByPipe } from '../shared/filter-by.pipe';

import { PedidosListComponent } from './pedidos-list/pedidos-list.component';
import { PedidosFormComponent } from './pedidos-form/pedidos-form.component';
import { SimpleConfirmDialog } from './shared/simple-confirm.dialog';
import { PedidosApiService } from './pedidos-api.service';

const routes: Routes = [
  { path: 'pedidos', component: PedidosListComponent, data: { title: 'Pedidos' } },
  { path: 'pedidos/novo', component: PedidosFormComponent, data: { title: 'Novo Pedido' } },
  { path: 'pedidos/editar/:id', component: PedidosFormComponent, data: { title: 'Editar Pedido' } }
];

@NgModule({
  declarations: [PedidosListComponent, PedidosFormComponent, SimpleConfirmDialog],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatCardModule,
    MatToolbarModule,
    MatDialogModule,
    MatSelectModule,
    MatAutocompleteModule,
    RouterModule.forChild(routes),
    FilterByPipe
  ],
  providers: [PedidosApiService]
})
export class PedidosModule {}



