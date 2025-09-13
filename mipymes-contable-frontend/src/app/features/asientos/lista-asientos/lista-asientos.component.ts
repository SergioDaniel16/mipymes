// src/app/features/asientos/lista-asientos/lista-asientos.component.ts
import { Component, OnInit } from '@angular/core';
import { AsientoContableService } from '../../../core/services/asiento-contable.service';
import { MatSnackBar } from '@angular/material/snack-bar';

// Interfaces para los DTOs (copiadas del servicio)
export interface AsientoContableDTO {
  id?: number;
  numeroAsiento?: number;
  fecha: string;
  descripcion: string;
  referencia?: string;
  tipo: string;
  totalDebitos: number;
  totalCreditos: number;
  estado?: string;
  movimientos?: MovimientoContableDTO[];
  fechaCreacion?: string;
  fechaModificacion?: string;
  creadoPor?: string;
  tipoDescripcion?: string;
  estadoDescripcion?: string;
  balanceado?: boolean;
}

export interface MovimientoContableDTO {
  id?: number;
  cuentaId: number;
  cuentaCodigo?: string;
  cuentaNombre?: string;
  tipoMovimiento: string;
  monto: number;
  descripcion?: string;
  orden?: number;
  fechaCreacion?: string;
  fechaModificacion?: string;
  tipoMovimientoDescripcion?: string;
}

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
              <input matInput
                     placeholder="Buscar asiento"
                     [(ngModel)]="textoBusqueda"
                     (keyup.enter)="buscarAsientos()">
              <button matSuffix mat-icon-button (click)="buscarAsientos()">
                <mat-icon>search</mat-icon>
              </button>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Fecha desde</mat-label>
              <input matInput
                     [matDatepicker]="picker1"
                     [(ngModel)]="fechaInicio"
                     (dateChange)="filtrarPorFechas()">
              <mat-datepicker-toggle matSuffix [for]="picker1"></mat-datepicker-toggle>
              <mat-datepicker #picker1></mat-datepicker>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Fecha hasta</mat-label>
              <input matInput
                     [matDatepicker]="picker2"
                     [(ngModel)]="fechaFin"
                     (dateChange)="filtrarPorFechas()">
              <mat-datepicker-toggle matSuffix [for]="picker2"></mat-datepicker-toggle>
              <mat-datepicker #picker2></mat-datepicker>
            </mat-form-field>

            <button mat-stroked-button (click)="limpiarFiltros()">
              <mat-icon>clear</mat-icon>
              Limpiar
            </button>
          </div>

          <!-- Indicador de carga -->
          <div *ngIf="cargando" class="loading-container">
            <mat-spinner diameter="40"></mat-spinner>
            <p>Cargando asientos...</p>
          </div>

          <!-- Mensaje cuando no hay datos -->
          <div *ngIf="!cargando && asientos.length === 0" class="no-data-container">
            <mat-icon>description</mat-icon>
            <p>No se encontraron asientos contables</p>
            <button mat-raised-button color="primary" routerLink="/asientos/nuevo">
              <mat-icon>add</mat-icon>
              Crear primer asiento
            </button>
          </div>

          <!-- Tabla de asientos -->
          <div class="table-container" *ngIf="!cargando && asientos.length > 0">
            <table mat-table [dataSource]="asientos" class="full-width">
              <ng-container matColumnDef="numero">
                <th mat-header-cell *matHeaderCellDef>No.</th>
                <td mat-cell *matCellDef="let asiento">{{ asiento.numeroAsiento }}</td>
              </ng-container>

              <ng-container matColumnDef="fecha">
                <th mat-header-cell *matHeaderCellDef>Fecha</th>
                <td mat-cell *matCellDef="let asiento">{{ asiento.fecha | date:'dd/MM/yyyy' }}</td>
              </ng-container>

              <ng-container matColumnDef="descripcion">
                <th mat-header-cell *matHeaderCellDef>Descripción</th>
                <td mat-cell *matCellDef="let asiento" class="descripcion-cell">
                  {{ asiento.descripcion }}
                  <small *ngIf="asiento.referencia">(Ref: {{ asiento.referencia }})</small>
                </td>
              </ng-container>

              <ng-container matColumnDef="tipo">
                <th mat-header-cell *matHeaderCellDef>Tipo</th>
                <td mat-cell *matCellDef="let asiento">
                  <mat-chip [color]="getTipoColor(asiento.tipo)" selected>
                    {{ asiento.tipoDescripcion || asiento.tipo }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="debitos">
                <th mat-header-cell *matHeaderCellDef>Débitos</th>
                <td mat-cell *matCellDef="let asiento" class="text-right">
                  {{ asiento.totalDebitos | currency:'GTQ':'symbol':'1.2-2' }}
                </td>
              </ng-container>

              <ng-container matColumnDef="creditos">
                <th mat-header-cell *matHeaderCellDef>Créditos</th>
                <td mat-cell *matCellDef="let asiento" class="text-right">
                  {{ asiento.totalCreditos | currency:'GTQ':'symbol':'1.2-2' }}
                </td>
              </ng-container>

              <ng-container matColumnDef="estado">
                <th mat-header-cell *matHeaderCellDef>Estado</th>
                <td mat-cell *matCellDef="let asiento">
                  <mat-chip [color]="getEstadoColor(asiento.estado)" selected>
                    {{ asiento.estadoDescripcion || asiento.estado }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="acciones">
                <th mat-header-cell *matHeaderCellDef>Acciones</th>
                <td mat-cell *matCellDef="let asiento">
                  <button mat-icon-button
                          [routerLink]="['/asientos/editar', asiento.id]"
                          matTooltip="Editar asiento">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button
                          color="primary"
                          (click)="contabilizar(asiento)"
                          [disabled]="asiento.estado === 'CONTABILIZADO'"
                          matTooltip="Contabilizar asiento">
                    <mat-icon>check_circle</mat-icon>
                  </button>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
            </table>
          </div>

          <!-- Resumen de totales -->
          <div *ngIf="!cargando && asientos.length > 0" class="totales-summary">
            <div class="summary-card">
              <h4>Resumen del Período</h4>
              <div class="summary-row">
                <span>Total Asientos:</span>
                <strong>{{ asientos.length }}</strong>
              </div>
              <div class="summary-row">
                <span>Total Débitos:</span>
                <strong>{{ getTotalDebitos() | currency:'GTQ':'symbol':'1.2-2' }}</strong>
              </div>
              <div class="summary-row">
                <span>Total Créditos:</span>
                <strong>{{ getTotalCreditos() | currency:'GTQ':'symbol':'1.2-2' }}</strong>
              </div>
              <div class="summary-row" [class]="getBalanceClass()">
                <span>Diferencia:</span>
                <strong>{{ getDiferencia() | currency:'GTQ':'symbol':'1.2-2' }}</strong>
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
      grid-template-columns: 2fr 1fr 1fr auto;
      gap: 16px;
      margin-bottom: 20px;
      align-items: start;
    }

    .table-container {
      overflow-x: auto;
      margin-bottom: 20px;
    }

    .text-right {
      text-align: right;
    }

    .descripcion-cell {
      max-width: 300px;
    }

    .descripcion-cell small {
      display: block;
      color: #666;
      font-size: 12px;
      margin-top: 2px;
    }

    .loading-container, .no-data-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 40px;
      text-align: center;
    }

    .no-data-container mat-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      color: #ccc;
      margin-bottom: 16px;
    }

    .totales-summary {
      margin-top: 20px;
      display: flex;
      justify-content: center;
    }

    .summary-card {
      background: #f5f5f5;
      border-radius: 8px;
      padding: 20px;
      min-width: 250px;
    }

    .summary-card h4 {
      margin: 0 0 16px 0;
      color: #4caf50;
      text-align: center;
    }

    .summary-row {
      display: flex;
      justify-content: space-between;
      margin: 8px 0;
      font-size: 14px;
    }

    .summary-row.balanced {
      color: #4caf50;
    }

    .summary-row.unbalanced {
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

      .filters-row {
        grid-template-columns: 1fr;
      }

      .summary-card {
        min-width: auto;
      }
    }
  `]
})
export class ListaAsientosComponent implements OnInit {
  displayedColumns: string[] = ['numero', 'fecha', 'descripcion', 'tipo', 'debitos', 'creditos', 'estado', 'acciones'];

  asientos: AsientoContableDTO[] = [];
  cargando = false;
  textoBusqueda = '';
  fechaInicio: Date | null = null;
  fechaFin: Date | null = null;

  constructor(
    private asientoService: AsientoContableService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.cargarAsientos();
  }

  cargarAsientos(): void {
    this.cargando = true;
    this.asientoService.obtenerLibroDiario().subscribe({
      next: (asientos) => {
        this.asientos = asientos;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar asientos:', error);
        this.snackBar.open('Error al cargar los asientos contables', 'Cerrar', { duration: 5000 });
        this.cargando = false;
      }
    });
  }

  buscarAsientos(): void {
    if (!this.textoBusqueda.trim()) {
      this.cargarAsientos();
      return;
    }

    this.cargando = true;
    this.asientoService.buscarAsientosPorDescripcion(this.textoBusqueda).subscribe({
      next: (asientos) => {
        this.asientos = asientos;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error en búsqueda:', error);
        this.snackBar.open('Error al buscar asientos', 'Cerrar', { duration: 5000 });
        this.cargando = false;
      }
    });
  }

  filtrarPorFechas(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      return;
    }

    this.cargando = true;
    const fechaInicioStr = this.fechaInicio.toISOString().split('T')[0];
    const fechaFinStr = this.fechaFin.toISOString().split('T')[0];

    this.asientoService.obtenerLibroDiarioPorPeriodo(fechaInicioStr, fechaFinStr).subscribe({
      next: (asientos) => {
        this.asientos = asientos;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al filtrar por fechas:', error);
        this.snackBar.open('Error al filtrar por fechas', 'Cerrar', { duration: 5000 });
        this.cargando = false;
      }
    });
  }

  limpiarFiltros(): void {
    this.textoBusqueda = '';
    this.fechaInicio = null;
    this.fechaFin = null;
    this.cargarAsientos();
  }

  contabilizar(asiento: AsientoContableDTO): void {
    if (!asiento.id) return;

    this.asientoService.contabilizarAsiento(asiento.id).subscribe({
      next: (asientoActualizado) => {
        // Actualizar el asiento en la lista
        const index = this.asientos.findIndex(a => a.id === asiento.id);
        if (index >= 0) {
          this.asientos[index] = asientoActualizado;
        }
        this.snackBar.open('Asiento contabilizado exitosamente', 'Cerrar', { duration: 3000 });
      },
      error: (error) => {
        console.error('Error al contabilizar:', error);
        this.snackBar.open('Error al contabilizar el asiento', 'Cerrar', { duration: 5000 });
      }
    });
  }

  // Métodos auxiliares
  getTipoColor(tipo: string): string {
    switch (tipo) {
      case 'APERTURA': return 'primary';
      case 'OPERACION': return 'accent';
      case 'AJUSTE': return 'warn';
      case 'CIERRE': return '';
      default: return '';
    }
  }

  getEstadoColor(estado: string): string {
    switch (estado) {
      case 'CONTABILIZADO': return 'primary';
      case 'VALIDADO': return 'accent';
      case 'BORRADOR': return 'warn';
      case 'ANULADO': return '';
      default: return '';
    }
  }

  getTotalDebitos(): number {
    return this.asientos.reduce((total, asiento) => total + asiento.totalDebitos, 0);
  }

  getTotalCreditos(): number {
    return this.asientos.reduce((total, asiento) => total + asiento.totalCreditos, 0);
  }

  getDiferencia(): number {
    return this.getTotalDebitos() - this.getTotalCreditos();
  }

  getBalanceClass(): string {
    const diferencia = this.getDiferencia();
    return Math.abs(diferencia) < 0.01 ? 'balanced' : 'unbalanced';
  }
}
