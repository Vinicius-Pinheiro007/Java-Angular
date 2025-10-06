import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-simple-confirm-dialog',
  template: `
  <h2 mat-dialog-title>{{data?.titulo || 'Confirmar'}}</h2>
  <mat-dialog-content>{{data?.mensagem || 'Tem certeza?'}}</mat-dialog-content>
  <mat-dialog-actions align="end">
    <button mat-button (click)="fechar(false)">Cancelar</button>
    <button mat-raised-button color="warn" (click)="fechar(true)">Excluir</button>
  </mat-dialog-actions>
  `
})
export class SimpleConfirmDialog {
  constructor(
    private dialogRef: MatDialogRef<SimpleConfirmDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  fechar(ok: boolean) {
    this.dialogRef.close(ok);
  }
}




