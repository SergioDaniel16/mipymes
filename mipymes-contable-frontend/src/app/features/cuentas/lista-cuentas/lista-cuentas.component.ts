// src/app/features/cuentas/lista-cuentas/lista-cuentas.component.ts
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-lista-cuentas',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Catálogo de Cuentas</h1>
        <button mat-raised-button color="primary" routerLink="/cuentas/nueva">
          <mat-icon>add</mat-icon>
          Nueva Cuenta
        </button>
      </div>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Cuentas Registradas</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <div class="search-bar">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Buscar cuenta</mat-label>
              <input matInput placeholder="Buscar por código o nombre">
              <mat-icon matSuffix>search</mat-icon>
            </mat-form-field>
          </div>

          <div class="table-container">
            <table mat-table [dataSource]="cuentas" class="full-width">
              <ng-container matColumnDef="codigo">
                <th mat-header-cell *matHeaderCellDef>Código</th>
                <td mat-cell *matCellDef="let cuenta">{{ cuenta.codigo }}</td>
              </ng-container>

              <ng-container matColumnDef="nombre">
                <th mat-header-cell *matHeaderCellDef>Nombre</th>
                <td mat-cell *matCellDef="let cuenta">{{ cuenta.nombre }}</td>
              </ng-container>

              <ng-container matColumnDef="tipo">
                <th mat-header-cell *matHeaderCellDef>Tipo</th>
                <td mat-cell *matCellDef="let cuenta">{{ cuenta.tipo }}</td>
              </ng-container>

              <ng-container matColumnDef="saldo">
                <th mat-header-cell *matHeaderCellDef>Saldo</th>
                <td mat-cell *matCellDef="let cuenta" class="text-right">{{ cuenta.saldo | currency:'GTQ':'symbol':'1.2-2' }}</td>
              </ng-container>

              <ng-container matColumnDef="acciones">
                <th mat-header-cell *matHeaderCellDef>Acciones</th>
                <td mat-cell *matCellDef="let cuenta">
                  <button mat-icon-button [routerLink]="['/cuentas/editar', cuenta.id]">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button color="warn" (click)="eliminarCuenta(cuenta)">
                    <mat-icon>delete</mat-icon>
                  </button>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
            </table>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
      max-width: 1200px;
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

    .search-bar {
      margin-bottom: 20px;
    }

    .table-container {
      overflow-x: auto;
    }

    .text-right {
      text-align: right;
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
    }
  `]
})
export class ListaCuentasComponent implements OnInit {
  displayedColumns: string[] = ['codigo', 'nombre', 'tipo', 'saldo', 'acciones'];

  // Datos de ejemplo basados en el caso "El Almacén El Planeador"
  cuentas = [
    { id: 1, codigo: '1001', nombre: 'Efectivo en Caja', tipo: 'ACTIVO', saldo: 38100 },
    { id: 2, codigo: '1002', nombre: 'Bancos', tipo: 'ACTIVO', saldo: 68000 },
    { id: 3, codigo: '1101', nombre: 'Inventario de Mercaderías', tipo: 'ACTIVO', saldo: 78000 },
    { id: 4, codigo: '1201', nombre: 'Mobiliario y Equipo', tipo: 'ACTIVO', saldo: 26000 },
    { id: 5, codigo: '2001', nombre: 'Proveedores', tipo: 'PASIVO', saldo: 37000 },
    { id: 6, codigo: '2002', nombre: 'Letras de Cambio por Pagar', tipo: 'PASIVO', saldo: 8000 },
    { id: 7, codigo: '3001', nombre: 'Capital', tipo: 'PATRIMONIO', saldo: 165100 },
    { id: 8, codigo: '4001', nombre: 'Ventas', tipo: 'INGRESO', saldo: 0 },
    { id: 9, codigo: '5001', nombre: 'Alquiler', tipo: 'GASTO', saldo: 0 },
    { id: 10, codigo: '5002', nombre: 'Publicidad', tipo: 'GASTO', saldo: 0 },
    { id: 11, codigo: '5003', nombre: 'Sueldos', tipo: 'GASTO', saldo: 0 }
  ];

  constructor() { }

  ngOnInit(): void {
    console.log('Lista de cuentas cargada');
  }

  eliminarCuenta(cuenta: any): void {
    console.log('Eliminar cuenta:', cuenta);
    // Aquí implementaremos la lógica de eliminación
  }
}
