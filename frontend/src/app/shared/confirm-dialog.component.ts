import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-dialog',
  template: `
  <h2 mat-dialog-title>{{data?.titulo || 'Confirmação'}}</h2>
  <mat-dialog-content>{{data?.mensagem || 'Deseja confirmar?'}}</mat-dialog-content>
  <mat-dialog-actions align="end">
    <button mat-button (click)="fechar(false)">Cancelar</button>
    <button mat-raised-button color="warn" (click)="fechar(true)">Confirmar</button>
  </mat-dialog-actions>
  `
})
export class ConfirmDialogComponent {
  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}
  fechar(ok: boolean) { this.dialogRef.close(ok); }
}



