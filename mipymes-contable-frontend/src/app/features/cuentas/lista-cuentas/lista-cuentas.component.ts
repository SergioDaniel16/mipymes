// src/app/features/cuentas/lista-cuentas/lista-cuentas.component.ts
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CuentaService } from '../../../core/services/cuenta.service';
import { CuentaDTO, getTipoDescripcion } from '../../../core/models/cuenta.model';

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
          <mat-card-subtitle *ngIf="cuentas.length > 0">
            Total: {{ cuentas.length }} cuentas
          </mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>

          <!-- Barra de búsqueda -->
          <div class="search-bar">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Buscar cuenta</mat-label>
              <input matInput
                     placeholder="Buscar por código o nombre"
                     [(ngModel)]="filtroTexto"
                     (input)="filtrarCuentas()">
              <mat-icon matSuffix>search</mat-icon>
            </mat-form-field>
          </div>

          <!-- Loading -->
          <div *ngIf="cargando" class="loading-container">
            <mat-spinner diameter="50"></mat-spinner>
            <p>Cargando cuentas...</p>
          </div>

          <!-- Error -->
          <div *ngIf="error && !cargando" class="error-container">
            <mat-icon color="warn">error</mat-icon>
            <p>{{ error }}</p>
            <button mat-raised-button color="primary" (click)="cargarCuentas()">
              Reintentar
            </button>
          </div>

          <!-- Tabla de cuentas -->
          <div *ngIf="!cargando && !error" class="table-container">
            <table mat-table [dataSource]="cuentasFiltradas" class="full-width">

              <ng-container matColumnDef="codigo">
                <th mat-header-cell *matHeaderCellDef>Código</th>
                <td mat-cell *matCellDef="let cuenta">
                  <strong>{{ cuenta.codigo }}</strong>
                </td>
              </ng-container>

              <ng-container matColumnDef="nombre">
                <th mat-header-cell *matHeaderCellDef>Nombre</th>
                <td mat-cell *matCellDef="let cuenta">{{ cuenta.nombre }}</td>
              </ng-container>

              <ng-container matColumnDef="tipo">
                <th mat-header-cell *matHeaderCellDef>Tipo</th>
                <td mat-cell *matCellDef="let cuenta">
                  <mat-chip [color]="getTipoColor(cuenta.tipo)" selected>
                    {{ getTipoDescripcion(cuenta.tipo) }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="saldo">
                <th mat-header-cell *matHeaderCellDef>Saldo</th>
                <td mat-cell *matCellDef="let cuenta" class="text-right">
                  <span [class]="getSaldoClass(cuenta.saldo)">
                    {{ cuenta.saldo | currency:'GTQ':'symbol':'1.2-2' }}
                  </span>
                </td>
              </ng-container>

              <ng-container matColumnDef="estado">
                <th mat-header-cell *matHeaderCellDef>Estado</th>
                <td mat-cell *matCellDef="let cuenta">
                  <mat-chip [color]="cuenta.activa ? 'primary' : 'warn'" selected>
                    {{ cuenta.activa ? 'Activa' : 'Inactiva' }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="acciones">
                <th mat-header-cell *matHeaderCellDef>Acciones</th>
                <td mat-cell *matCellDef="let cuenta">
                  <button mat-icon-button
                          [routerLink]="['/cuentas/editar', cuenta.id]"
                          matTooltip="Editar cuenta">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button
                          color="warn"
                          (click)="confirmarEliminar(cuenta)"
                          [disabled]="!cuenta.activa"
                          matTooltip="Desactivar cuenta">
                    <mat-icon>delete</mat-icon>
                  </button>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"
                  [class.inactive-row]="!row.activa"></tr>
            </table>

            <!-- Mensaje cuando no hay cuentas -->
            <div *ngIf="cuentasFiltradas.length === 0" class="empty-state">
              <mat-icon>account_tree</mat-icon>
              <h3>No se encontraron cuentas</h3>
              <p *ngIf="filtroTexto">Intenta cambiar el término de búsqueda</p>
              <p *ngIf="!filtroTexto">Comienza creando tu primera cuenta</p>
              <button mat-raised-button color="primary" routerLink="/cuentas/nueva">
                <mat-icon>add</mat-icon>
                Crear Primera Cuenta
              </button>
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

    .search-bar {
      margin-bottom: 20px;
    }

    .table-container {
      overflow-x: auto;
    }

    .text-right {
      text-align: right;
    }

    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px;
    }

    .loading-container p {
      margin-top: 16px;
      color: #666;
    }

    .error-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px;
      text-align: center;
    }

    .error-container mat-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      margin-bottom: 16px;
    }

    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 60px 20px;
      text-align: center;
    }

    .empty-state mat-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      color: #ccc;
      margin-bottom: 16px;
    }

    .empty-state h3 {
      margin: 0 0 8px 0;
      color: #666;
    }

    .empty-state p {
      margin: 0 0 24px 0;
      color: #999;
    }

    .inactive-row {
      opacity: 0.6;
      background-color: #f5f5f5;
    }

    .saldo-positivo {
      color: #4caf50;
      font-weight: 500;
    }

    .saldo-negativo {
      color: #f44336;
      font-weight: 500;
    }

    .saldo-cero {
      color: #666;
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

      .empty-state {
        padding: 40px 20px;
      }
    }
  `]
})
export class ListaCuentasComponent implements OnInit {
  displayedColumns: string[] = ['codigo', 'nombre', 'tipo', 'saldo', 'estado', 'acciones'];

  cuentas: CuentaDTO[] = [];
  cuentasFiltradas: CuentaDTO[] = [];
  filtroTexto: string = '';
  cargando: boolean = false;
  error: string | null = null;

  constructor(
    private cuentaService: CuentaService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.cargarCuentas();
  }

  /**
   * Cargar cuentas desde el backend
   */
  cargarCuentas(): void {
    this.cargando = true;
    this.error = null;

    this.cuentaService.obtenerTodasLasCuentas().subscribe({
      next: (cuentas) => {
        console.log('Cuentas cargadas:', cuentas);
        this.cuentas = cuentas;
        this.cuentasFiltradas = [...cuentas];
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar cuentas:', error);
        this.error = 'Error al cargar las cuentas. Verifique la conexión con el servidor.';
        this.cargando = false;
        this.mostrarError('Error al cargar las cuentas');
      }
    });
  }

  /**
   * Filtrar cuentas por texto
   */
  filtrarCuentas(): void {
    if (!this.filtroTexto.trim()) {
      this.cuentasFiltradas = [...this.cuentas];
      return;
    }

    const filtro = this.filtroTexto.toLowerCase().trim();
    this.cuentasFiltradas = this.cuentas.filter(cuenta =>
      cuenta.codigo.toLowerCase().includes(filtro) ||
      cuenta.nombre.toLowerCase().includes(filtro)
    );
  }

  /**
   * Confirmar eliminación de cuenta
   */
  confirmarEliminar(cuenta: CuentaDTO): void {
    if (confirm(`¿Está seguro de desactivar la cuenta "${cuenta.codigo} - ${cuenta.nombre}"?`)) {
      this.eliminarCuenta(cuenta);
    }
  }

  /**
   * Eliminar (desactivar) cuenta
   */
  eliminarCuenta(cuenta: CuentaDTO): void {
    if (!cuenta.id) return;

    this.cuentaService.desactivarCuenta(cuenta.id).subscribe({
      next: () => {
        this.mostrarExito('Cuenta desactivada exitosamente');
        this.cargarCuentas(); // Recargar la lista
      },
      error: (error) => {
        console.error('Error al desactivar cuenta:', error);
        this.mostrarError('Error al desactivar la cuenta');
      }
    });
  }

  /**
   * Obtener color del chip según el tipo
   */
  getTipoColor(tipo: string): string {
    const colores: { [key: string]: string } = {
      'ACTIVO': 'primary',
      'PASIVO': 'accent',
      'PATRIMONIO': 'warn',
      'INGRESO': 'primary',
      'GASTO': 'accent'
    };
    return colores[tipo] || '';
  }

  /**
   * Obtener clase CSS según el saldo
   */
  getSaldoClass(saldo: number): string {
    if (saldo > 0) return 'saldo-positivo';
    if (saldo < 0) return 'saldo-negativo';
    return 'saldo-cero';
  }

  /**
   * Obtener descripción del tipo (importado del modelo)
   */
  getTipoDescripcion = getTipoDescripcion;

  /**
   * Mostrar mensaje de éxito
   */
  private mostrarExito(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  /**
   * Mostrar mensaje de error
   */
  private mostrarError(mensaje: string): void {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}
