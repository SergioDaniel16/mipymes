// src/app/features/asientos/lista-asientos/lista-asientos.component.ts
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-lista-asientos',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Libro Diario</h1>
        <button mat-raised-button color="primary" routerLink="/asientos/nuevo">
          <mat-icon>add</mat-icon>
          Nuevo Asiento
        </button>
      </div>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Asientos Contables Registrados</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <div class="filters-row">
            <mat-form-field appearance="outline">
              <mat-label>Buscar por descripción</mat-label>
              <input matInput placeholder="Buscar asiento">
              <mat-icon matSuffix>search</mat-icon>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Fecha desde</mat-label>
              <input matInput [matDatepicker]="picker1">
              <mat-datepicker-toggle matSuffix [for]="picker1"></mat-datepicker-toggle>
              <mat-datepicker #picker1></mat-datepicker>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Fecha hasta</mat-label>
              <input matInput [matDatepicker]="picker2">
              <mat-datepicker-toggle matSuffix [for]="picker2"></mat-datepicker-toggle>
              <mat-datepicker #picker2></mat-datepicker>
            </mat-form-field>
          </div>

          <div class="table-container">
            <table mat-table [dataSource]="asientos" class="full-width">
              <ng-container matColumnDef="numero">
                <th mat-header-cell *matHeaderCellDef>No.</th>
                <td mat-cell *matCellDef="let asiento">{{ asiento.numero }}</td>
              </ng-container>

              <ng-container matColumnDef="fecha">
                <th mat-header-cell *matHeaderCellDef>Fecha</th>
                <td mat-cell *matCellDef="let asiento">{{ asiento.fecha | date:'dd/MM/yyyy' }}</td>
              </ng-container>

              <ng-container matColumnDef="descripcion">
                <th mat-header-cell *matHeaderCellDef>Descripción</th>
                <td mat-cell *matCellDef="let asiento">{{ asiento.descripcion }}</td>
              </ng-container>

              <ng-container matColumnDef="debitos">
                <th mat-header-cell *matHeaderCellDef>Débitos</th>
                <td mat-cell *matCellDef="let asiento" class="text-right">{{ asiento.debitos | currency:'GTQ':'symbol':'1.2-2' }}</td>
              </ng-container>

              <ng-container matColumnDef="creditos">
                <th mat-header-cell *matHeaderCellDef>Créditos</th>
                <td mat-cell *matCellDef="let asiento" class="text-right">{{ asiento.creditos | currency:'GTQ':'symbol':'1.2-2' }}</td>
              </ng-container>

              <ng-container matColumnDef="estado">
                <th mat-header-cell *matHeaderCellDef>Estado</th>
                <td mat-cell *matCellDef="let asiento">
                  <mat-chip [color]="getEstadoColor(asiento.estado)" selected>
                    {{ asiento.estado }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="acciones">
                <th mat-header-cell *matHeaderCellDef>Acciones</th>
                <td mat-cell *matCellDef="let asiento">
                  <button mat-icon-button [routerLink]="['/asientos/editar', asiento.id]">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button color="primary" (click)="contabilizar(asiento)"
                          [disabled]="asiento.estado === 'CONTABILIZADO'">
                    <mat-icon>check_circle</mat-icon>
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

    .filters-row {
      display: grid;
      grid-template-columns: 2fr 1fr 1fr;
      gap: 16px;
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

      .filters-row {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class ListaAsientosComponent implements OnInit {
  displayedColumns: string[] = ['numero', 'fecha', 'descripcion', 'debitos', 'creditos', 'estado', 'acciones'];

  // Datos de ejemplo basados en el caso "El Almacén El Planeador"
  asientos = [
    {
      id: 1,
      numero: 1,
      fecha: new Date('2024-01-05'),
      descripcion: 'Depósito bancario por Q12,000.00',
      debitos: 12000,
      creditos: 12000,
      estado: 'CONTABILIZADO'
    },
    {
      id: 2,
      numero: 2,
      fecha: new Date('2024-01-10'),
      descripcion: 'Pago de alquiler del local Q4,000.00',
      debitos: 4000,
      creditos: 4000,
      estado: 'CONTABILIZADO'
    },
    {
      id: 3,
      numero: 3,
      fecha: new Date('2024-01-15'),
      descripcion: 'Compra de mercaderías por Q7,000.00',
      debitos: 7000,
      creditos: 7000,
      estado: 'BORRADOR'
    }
  ];

  constructor() { }

  ngOnInit(): void {
    console.log('Lista de asientos cargada');
  }

  getEstadoColor(estado: string): string {
    switch (estado) {
      case 'CONTABILIZADO': return '#4caf50';
      case 'VALIDADO': return '#ff9800';
      case 'BORRADOR': return '#f44336';
      default: return '#757575';
    }
  }

  contabilizar(asiento: any): void {
    console.log('Contabilizar asiento:', asiento);
    // Aquí implementaremos la lógica de contabilización
  }
}
