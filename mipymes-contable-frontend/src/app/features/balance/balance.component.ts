// src/app/features/balance/balance.component.ts
import { Component, OnInit } from '@angular/core';

interface LineaBalance {
  codigo: string;
  nombreCuenta: string;
  tipoCuenta: string;
  naturaleza: string;
  saldoDeudor: number;
  saldoAcreedor: number;
}

interface TotalesBalance {
  totalDeudores: number;
  totalAcreedores: number;
  diferencia: number;
  totalCuentas: number;
  balanceado: boolean;
}

@Component({
  selector: 'app-balance',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Balance de Comprobación</h1>
        <div class="header-actions">
          <button mat-raised-button color="primary" (click)="generarBalance()">
            <mat-icon>refresh</mat-icon>
            Actualizar Balance
          </button>
          <button mat-stroked-button (click)="exportarPDF()">
            <mat-icon>picture_as_pdf</mat-icon>
            Exportar PDF
          </button>
        </div>
      </div>

      <!-- Información del reporte -->
      <mat-card class="info-card">
        <mat-card-header>
          <mat-card-title>Almacén El Planeador</mat-card-title>
          <mat-card-subtitle>Balance de Comprobación al {{ fechaActual | date:'dd/MM/yyyy' }}</mat-card-subtitle>
        </mat-card-header>
      </mat-card>

      <!-- Tabla del balance -->
      <mat-card>
        <mat-card-content>
          <div class="table-container">
            <table mat-table [dataSource]="lineasBalance" class="full-width balance-table">

              <!-- Columna Código -->
              <ng-container matColumnDef="codigo">
                <th mat-header-cell *matHeaderCellDef class="codigo-column">Código</th>
                <td mat-cell *matCellDef="let linea" class="codigo-cell">{{ linea.codigo }}</td>
                <td mat-footer-cell *matFooterCellDef class="total-label"><strong>TOTALES</strong></td>
              </ng-container>

              <!-- Columna Nombre de Cuenta -->
              <ng-container matColumnDef="nombreCuenta">
                <th mat-header-cell *matHeaderCellDef>Nombre de la Cuenta</th>
                <td mat-cell *matCellDef="let linea">{{ linea.nombreCuenta }}</td>
                <td mat-footer-cell *matFooterCellDef></td>
              </ng-container>

              <!-- Columna Tipo -->
              <ng-container matColumnDef="tipo">
                <th mat-header-cell *matHeaderCellDef class="tipo-column">Tipo</th>
                <td mat-cell *matCellDef="let linea" class="tipo-cell">
                  <mat-chip [color]="getTipoColor(linea.tipoCuenta)" selected>
                    {{ linea.tipoCuenta }}
                  </mat-chip>
                </td>
                <td mat-footer-cell *matFooterCellDef></td>
              </ng-container>

              <!-- Columna Saldos Deudores -->
              <ng-container matColumnDef="saldoDeudor">
                <th mat-header-cell *matHeaderCellDef class="saldo-column">Saldos Deudores</th>
                <td mat-cell *matCellDef="let linea" class="saldo-cell">
                  <span *ngIf="linea.saldoDeudor > 0">{{ linea.saldoDeudor | currency:'GTQ':'symbol':'1.2-2' }}</span>
                </td>
                <td mat-footer-cell *matFooterCellDef class="total-deudor">
                  <strong>{{ totales.totalDeudores | currency:'GTQ':'symbol':'1.2-2' }}</strong>
                </td>
              </ng-container>

              <!-- Columna Saldos Acreedores -->
              <ng-container matColumnDef="saldoAcreedor">
                <th mat-header-cell *matHeaderCellDef class="saldo-column">Saldos Acreedores</th>
                <td mat-cell *matCellDef="let linea" class="saldo-cell">
                  <span *ngIf="linea.saldoAcreedor > 0">{{ linea.saldoAcreedor | currency:'GTQ':'symbol':'1.2-2' }}</span>
                </td>
                <td mat-footer-cell *matFooterCellDef class="total-acreedor">
                  <strong>{{ totales.totalAcreedores | currency:'GTQ':'symbol':'1.2-2' }}</strong>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"
                  [class.highlight-row]="row.saldoDeudor > 0 || row.saldoAcreedor > 0"></tr>
              <tr mat-footer-row *matFooterRowDef="displayedColumns" class="totales-row"></tr>
            </table>
          </div>

          <!-- Resumen del balance -->
          <div class="balance-summary">
            <div class="summary-card" [ngClass]="{'balanced': totales.balanceado, 'unbalanced': !totales.balanceado}">
              <h3>Estado del Balance</h3>
              <div class="summary-row">
                <span>Total Cuentas: <strong>{{ totales.totalCuentas }}</strong></span>
              </div>
              <div class="summary-row">
                <span>Diferencia:
                  <strong [class]="totales.balanceado ? 'success-text' : 'error-text'">
                    {{ totales.diferencia | currency:'GTQ':'symbol':'1.2-2' }}
                  </strong>
                </span>
              </div>
              <div class="balance-status">
                <mat-icon [class]="totales.balanceado ? 'success-icon' : 'error-icon'">
                  {{ totales.balanceado ? 'check_circle' : 'error' }}
                </mat-icon>
                <span [class]="totales.balanceado ? 'success-text' : 'error-text'">
                  {{ totales.balanceado ? 'BALANCE CORRECTO' : 'BALANCE DESCUADRADO' }}
                </span>
              </div>
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
      max-width: 1400px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .page-header h1 {
      margin: 0;
      color: #4caf50;
    }

    .header-actions {
      display: flex;
      gap: 12px;
    }

    .info-card .mat-mdc-card-header {
      background: linear-gradient(135deg, #4caf50 0%, #66bb6a 100%);
      color: white;
      margin: -24px -24px 24px -24px;
      padding: 20px 24px;
      border-radius: 4px 4px 0 0;
    }

    .table-container {
      overflow-x: auto;
      margin-bottom: 20px;
    }

    .balance-table {
      width: 100%;
      border: 1px solid #e0e0e0;
    }

    .codigo-column, .tipo-column {
      width: 100px;
    }

    .saldo-column {
      width: 150px;
      text-align: right;
    }

    .codigo-cell, .tipo-cell {
      font-weight: 500;
    }

    .saldo-cell {
      text-align: right;
      font-family: 'Courier New', monospace;
    }

    .totales-row {
      background-color: #f5f5f5;
      font-weight: bold;
      border-top: 2px solid #4caf50;
    }

    .total-label {
      color: #4caf50;
      font-weight: bold;
    }

    .total-deudor, .total-acreedor {
      text-align: right;
      color: #4caf50;
      font-family: 'Courier New', monospace;
    }

    .highlight-row:hover {
      background-color: #f8f8f8;
    }

    .balance-summary {
      margin-top: 20px;
      display: flex;
      justify-content: center;
    }

    .summary-card {
      padding: 20px;
      border-radius: 8px;
      text-align: center;
      min-width: 300px;
    }

    .summary-card.balanced {
      background-color: #e8f5e8;
      border: 2px solid #4caf50;
    }

    .summary-card.unbalanced {
      background-color: #ffebee;
      border: 2px solid #f44336;
    }

    .summary-card h3 {
      margin: 0 0 16px 0;
      color: #333;
    }

    .summary-row {
      margin: 8px 0;
      font-size: 16px;
    }

    .balance-status {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      margin-top: 16px;
      font-size: 18px;
      font-weight: 500;
    }

    .success-text, .success-icon {
      color: #4caf50;
    }

    .error-text, .error-icon {
      color: #f44336;
    }

    @media (max-width: 768px) {
      .page-container {
        padding: 16px;
      }

      .page-header {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
      }

      .header-actions {
        justify-content: space-between;
      }

      .codigo-column, .tipo-column {
        width: 80px;
      }

      .saldo-column {
        width: 120px;
      }

      .summary-card {
        min-width: auto;
      }
    }
  `]
})
export class BalanceComponent implements OnInit {
  displayedColumns: string[] = ['codigo', 'nombreCuenta', 'tipo', 'saldoDeudor', 'saldoAcreedor'];
  fechaActual = new Date();

  // Datos de ejemplo basados en "El Almacén El Planeador"
  lineasBalance: LineaBalance[] = [
    { codigo: '1001', nombreCuenta: 'Efectivo en Caja', tipoCuenta: 'ACTIVO', naturaleza: 'DEUDORA', saldoDeudor: 38100, saldoAcreedor: 0 },
    { codigo: '1002', nombreCuenta: 'Bancos', tipoCuenta: 'ACTIVO', naturaleza: 'DEUDORA', saldoDeudor: 68000, saldoAcreedor: 0 },
    { codigo: '1101', nombreCuenta: 'Inventario de Mercaderías', tipoCuenta: 'ACTIVO', naturaleza: 'DEUDORA', saldoDeudor: 86000, saldoAcreedor: 0 },
    { codigo: '1201', nombreCuenta: 'Mobiliario y Equipo', tipoCuenta: 'ACTIVO', naturaleza: 'DEUDORA', saldoDeudor: 29000, saldoAcreedor: 0 },
    { codigo: '2001', nombreCuenta: 'Proveedores', tipoCuenta: 'PASIVO', naturaleza: 'ACREEDORA', saldoDeudor: 0, saldoAcreedor: 45000 },
    { codigo: '2002', nombreCuenta: 'Letras de Cambio por Pagar', tipoCuenta: 'PASIVO', naturaleza: 'ACREEDORA', saldoDeudor: 0, saldoAcreedor: 6000 },
    { codigo: '3001', nombreCuenta: 'Capital', tipoCuenta: 'PATRIMONIO', naturaleza: 'ACREEDORA', saldoDeudor: 0, saldoAcreedor: 165100 },
    { codigo: '4001', nombreCuenta: 'Ventas', tipoCuenta: 'INGRESO', naturaleza: 'ACREEDORA', saldoDeudor: 0, saldoAcreedor: 44000 },
    { codigo: '5001', nombreCuenta: 'Alquiler', tipoCuenta: 'GASTO', naturaleza: 'DEUDORA', saldoDeudor: 4000, saldoAcreedor: 0 },
    { codigo: '5002', nombreCuenta: 'Publicidad', tipoCuenta: 'GASTO', naturaleza: 'DEUDORA', saldoDeudor: 1580, saldoAcreedor: 0 },
    { codigo: '5003', nombreCuenta: 'Sueldos', tipoCuenta: 'GASTO', naturaleza: 'DEUDORA', saldoDeudor: 10400, saldoAcreedor: 0 }
  ];

  totales: TotalesBalance = {
    totalDeudores: 0,
    totalAcreedores: 0,
    diferencia: 0,
    totalCuentas: 0,
    balanceado: false
  };

  constructor() { }

  ngOnInit(): void {
    this.calcularTotales();
  }

  calcularTotales(): void {
    this.totales.totalDeudores = this.lineasBalance.reduce((sum, linea) => sum + linea.saldoDeudor, 0);
    this.totales.totalAcreedores = this.lineasBalance.reduce((sum, linea) => sum + linea.saldoAcreedor, 0);
    this.totales.diferencia = this.totales.totalDeudores - this.totales.totalAcreedores;
    this.totales.totalCuentas = this.lineasBalance.length;
    this.totales.balanceado = Math.abs(this.totales.diferencia) < 0.01;
  }

  getTipoColor(tipo: string): string {
    switch (tipo) {
      case 'ACTIVO': return 'primary';
      case 'PASIVO': return 'accent';
      case 'PATRIMONIO': return 'warn';
      case 'INGRESO': return 'primary';
      case 'GASTO': return 'accent';
      default: return '';
    }
  }

  generarBalance(): void {
    console.log('Generando balance actualizado...');
    this.calcularTotales();
  }

  exportarPDF(): void {
    console.log('Exportando balance a PDF...');
  }
}
