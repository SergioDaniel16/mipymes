// src/app/features/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  template: `
    <div class="dashboard-container">
      <div class="dashboard-header">
        <h1>Dashboard - Almacén El Planeador</h1>
        <p>Bienvenido al sistema contable</p>
      </div>

      <div class="dashboard-grid">
        <!-- Tarjetas de resumen -->
        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>account_balance_wallet</mat-icon>
              Efectivo en Caja
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="card-value">Q 38,100.00</div>
            <div class="card-subtitle">Saldo inicial</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>account_balance</mat-icon>
              Cuenta Bancaria
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="card-value">Q 68,000.00</div>
            <div class="card-subtitle">Saldo en banco</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>inventory</mat-icon>
              Inventario
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="card-value">Q 78,000.00</div>
            <div class="card-subtitle">Mercaderías</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>people</mat-icon>
              Proveedores
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="card-value">Q 37,000.00</div>
            <div class="card-subtitle">Cuentas por pagar</div>
          </mat-card-content>
        </mat-card>
      </div>

      <!-- Acciones rápidas -->
      <div class="quick-actions">
        <h2>Acciones Rápidas</h2>
        <div class="actions-grid">
          <button mat-raised-button color="primary" routerLink="/asientos">
            <mat-icon>book</mat-icon>
            Nuevo Asiento
          </button>
          <button mat-raised-button color="accent" routerLink="/cuentas">
            <mat-icon>account_tree</mat-icon>
            Ver Cuentas
          </button>
          <button mat-raised-button routerLink="/balance">
            <mat-icon>balance</mat-icon>
            Balance
          </button>
          <button mat-raised-button routerLink="/inventario">
            <mat-icon>inventory</mat-icon>
            Inventario
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }

    .dashboard-header {
      margin-bottom: 30px;
      text-align: center;
    }

    .dashboard-header h1 {
      margin: 0 0 8px 0;
      color: #4caf50;
    }

    .dashboard-header p {
      margin: 0;
      color: #666;
      font-size: 16px;
    }

    .dashboard-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin-bottom: 40px;
    }

    .dashboard-card {
      transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .dashboard-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }

    .dashboard-card mat-card-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
    }

    .dashboard-card mat-icon {
      color: #4caf50;
    }

    .card-value {
      font-size: 24px;
      font-weight: bold;
      color: #4caf50;
      margin-bottom: 4px;
    }

    .card-subtitle {
      color: #666;
      font-size: 14px;
    }

    .quick-actions {
      text-align: center;
    }

    .quick-actions h2 {
      margin-bottom: 20px;
      color: #333;
    }

    .actions-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      gap: 15px;
      max-width: 600px;
      margin: 0 auto;
    }

    .actions-grid button {
      padding: 12px 16px;
      font-size: 14px;
    }

    .actions-grid button mat-icon {
      margin-right: 8px;
    }

    @media (max-width: 768px) {
      .dashboard-container {
        padding: 16px;
      }

      .dashboard-grid {
        grid-template-columns: 1fr;
      }

      .actions-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class DashboardComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
    console.log('Dashboard component loaded!');
  }
}
