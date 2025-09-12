// src/app/core/layout/sidebar/sidebar.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface MenuItem {
  title: string;
  icon: string;
  route: string;
  description: string;
}

@Component({
  selector: 'app-sidebar',
  template: `
    <div class="sidebar-header">
      <mat-icon class="sidebar-icon">store</mat-icon>
      <h3 class="sidebar-title">El Planeador</h3>
      <p class="sidebar-subtitle">Sistema Contable</p>
    </div>

    <mat-nav-list class="sidebar-nav">
      <a mat-list-item
         *ngFor="let item of menuItems"
         [routerLink]="item.route"
         routerLinkActive="active-link"
         class="nav-item">
        <mat-icon matListIcon>{{ item.icon }}</mat-icon>
        <span matLine>{{ item.title }}</span>
        <span matLine class="item-description">{{ item.description }}</span>
      </a>
    </mat-nav-list>

    <div class="sidebar-footer">
      <mat-divider></mat-divider>
      <div class="footer-content">
        <p class="version">v{{ version }}</p>
        <p class="company">Horacio Porras</p>
      </div>
    </div>
  `,
  styles: [`
    .sidebar-header {
      padding: 24px 16px;
      text-align: center;
      background: linear-gradient(135deg, #4caf50 0%, #66bb6a 100%);
      color: white;
      margin-bottom: 16px;
    }

    .sidebar-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      margin-bottom: 8px;
    }

    .sidebar-title {
      margin: 0 0 4px 0;
      font-size: 20px;
      font-weight: 500;
    }

    .sidebar-subtitle {
      margin: 0;
      font-size: 14px;
      opacity: 0.9;
    }

    .sidebar-nav {
      padding: 0;
    }

    .nav-item {
      margin-bottom: 4px;
      border-radius: 8px;
      margin-left: 8px;
      margin-right: 8px;
      transition: all 0.2s ease;
    }

    .nav-item:hover {
      background-color: rgba(76, 175, 80, 0.1);
      transform: translateX(4px);
    }

    .active-link {
      background-color: rgba(76, 175, 80, 0.15);
      color: #4caf50;
      font-weight: 500;
    }

    .item-description {
      font-size: 12px;
      color: rgba(0,0,0,0.6);
      margin-top: 2px;
    }

    .active-link .item-description {
      color: rgba(76, 175, 80, 0.8);
    }

    .sidebar-footer {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 16px;
    }

    .footer-content {
      text-align: center;
      margin-top: 8px;
    }

    .version, .company {
      margin: 4px 0;
      font-size: 12px;
      color: rgba(0,0,0,0.6);
    }

    .company {
      font-weight: 500;
    }
  `]
})
export class SidebarComponent implements OnInit {
  version = environment.version;

  menuItems: MenuItem[] = [
    {
      title: 'Dashboard',
      icon: 'dashboard',
      route: '/dashboard',
      description: 'Panel principal'
    },
    {
      title: 'Cuentas',
      icon: 'account_tree',
      route: '/cuentas',
      description: 'Catálogo de cuentas'
    },
    {
      title: 'Asientos',
      icon: 'book',
      route: '/asientos',
      description: 'Libro diario'
    },
    {
      title: 'Balance',
      icon: 'balance',
      route: '/balance',
      description: 'Balance de comprobación'
    },
    {
      title: 'Estados Financieros',
      icon: 'assessment',
      route: '/estados-financieros',
      description: 'Balance general y resultados'
    },
    {
      title: 'Inventario',
      icon: 'inventory',
      route: '/inventario',
      description: 'Control de productos'
    },
    {
      title: 'Bancos',
      icon: 'account_balance',
      route: '/bancos',
      description: 'Cuentas bancarias'
    },
    {
      title: 'Clientes y Proveedores',
      icon: 'people',
      route: '/clientes-proveedores',
      description: 'CxC y CxP'
    }
  ];

  constructor(private router: Router) {}

  ngOnInit(): void {}
}
