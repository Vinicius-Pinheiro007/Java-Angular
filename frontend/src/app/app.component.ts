import { Component } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  template: `
  <mat-toolbar color="primary">
    <button mat-icon-button (click)="toggleSidenav()"><mat-icon>menu</mat-icon></button>
    <span>{{pageTitle || 'App'}}</span>
  </mat-toolbar>
  <mat-sidenav-container class="layout">
    <mat-sidenav #sidenav mode="side" [(opened)]="opened">
      <mat-nav-list>
        <a mat-list-item routerLink="/pedidos" routerLinkActive="active" (click)="opened=false"><mat-icon>receipt_long</mat-icon><span>Pedidos</span></a>
        <a mat-list-item routerLink="/produtos" routerLinkActive="active" (click)="opened=false"><mat-icon>inventory_2</mat-icon><span>Produtos</span></a>
        <a mat-list-item routerLink="/clientes" routerLinkActive="active" (click)="opened=false"><mat-icon>groups</mat-icon><span>Clientes</span></a>
      </mat-nav-list>
    </mat-sidenav>
    <mat-sidenav-content>
      <div class="container">
        <router-outlet></router-outlet>
      </div>
    </mat-sidenav-content>
  </mat-sidenav-container>
  `,
  styles: [`.layout{height:calc(100vh - 64px)} .container{padding:16px} mat-nav-list a{gap:8px} a.active{background:#e3f2fd}`]
})
export class AppComponent {
  pageTitle = '';
  opened = true;
  constructor(private router: Router, private route: ActivatedRoute) {
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      map(() => this.route),
      map(r => {
        while (r.firstChild) r = r.firstChild;
        return r;
      }),
      mergeMap(r => r.data)
    ).subscribe(data => this.pageTitle = data['title'] || 'App');
  }
  toggleSidenav() { this.opened = !this.opened; }
}



