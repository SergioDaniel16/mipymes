// src/app/features/asientos/form-asiento/form-asiento.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-form-asiento',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Nuevo Asiento Contable</h1>
        <button mat-stroked-button routerLink="/asientos">
          <mat-icon>arrow_back</mat-icon>
          Volver
        </button>
      </div>

      <mat-card>
        <mat-card-header>
          <mat-card-title>Información del Asiento</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="asientoForm" class="form-container">
            <!-- Cabecera del asiento -->
            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Fecha</mat-label>
                <input matInput [matDatepicker]="picker" formControlName="fecha">
                <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
                <mat-datepicker #picker></mat-datepicker>
              </mat-form-field>

              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Tipo de Asiento</mat-label>
                <mat-select formControlName="tipo">
                  <mat-option value="OPERACION">Operación</mat-option>
                  <mat-option value="AJUSTE">Ajuste</mat-option>
                  <mat-option value="APERTURA">Apertura</mat-option>
                  <mat-option value="CIERRE">Cierre</mat-option>
                </mat-select>
              </mat-form-field>
            </div>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Descripción</mat-label>
              <input matInput formControlName="descripcion" placeholder="Ej: Depósito bancario por Q12,000.00">
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Referencia (Opcional)</mat-label>
              <input matInput formControlName="referencia" placeholder="Número de documento, factura, etc.">
            </mat-form-field>

            <!-- Movimientos del asiento -->
            <div class="movimientos-section">
              <div class="section-header">
                <h3>Movimientos</h3>
                <button mat-raised-button type="button" color="accent" (click)="agregarMovimiento()">
                  <mat-icon>add</mat-icon>
                  Agregar Línea
                </button>
              </div>

              <div class="movimientos-container" formArrayName="movimientos">
                <div *ngFor="let movimiento of movimientos.controls; let i = index"
                     [formGroupName]="i" class="movimiento-row">

                  <mat-form-field appearance="outline" class="cuenta-field">
                    <mat-label>Cuenta</mat-label>
                    <mat-select formControlName="cuentaId">
                      <mat-option *ngFor="let cuenta of cuentasDisponibles" [value]="cuenta.id">
                        {{ cuenta.codigo }} - {{ cuenta.nombre }}
                      </mat-option>
                    </mat-select>
                  </mat-form-field>

                  <mat-form-field appearance="outline" class="tipo-field">
                    <mat-label>Tipo</mat-label>
                    <mat-select formControlName="tipo">
                      <mat-option value="DEBITO">Débito</mat-option>
                      <mat-option value="CREDITO">Crédito</mat-option>
                    </mat-select>
                  </mat-form-field>

                  <mat-form-field appearance="outline" class="monto-field">
                    <mat-label>Monto</mat-label>
                    <input matInput type="number" formControlName="monto" step="0.01" min="0">
                  </mat-form-field>

                  <mat-form-field appearance="outline" class="descripcion-field">
                    <mat-label>Descripción (Opcional)</mat-label>
                    <input matInput formControlName="descripcion">
                  </mat-form-field>

                  <button mat-icon-button type="button" color="warn"
                          (click)="eliminarMovimiento(i)"
                          [disabled]="movimientos.length <= 2">
                    <mat-icon>delete</mat-icon>
                  </button>
                </div>
              </div>

              <!-- Totales -->
              <div class="totales-section">
                <div class="totales-row">
                  <span>Total Débitos: {{ totalDebitos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                  <span>Total Créditos: {{ totalCreditos | currency:'GTQ':'symbol':'1.2-2' }}</span>
                  <span [class]="diferencia === 0 ? 'balanced' : 'unbalanced'">
                    Diferencia: {{ diferencia | currency:'GTQ':'symbol':'1.2-2' }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Acciones del formulario -->
            <div class="form-actions">
              <button mat-raised-button color="primary" type="submit"
                      [disabled]="!asientoForm.valid || diferencia !== 0"
                      (click)="guardar()">
                <mat-icon>save</mat-icon>
                Guardar Asiento
              </button>
              <button mat-stroked-button type="button" routerLink="/asientos">
                Cancelar
              </button>
            </div>
          </form>
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

    .form-container {
      padding: 20px 0;
    }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
      margin-bottom: 16px;
    }

    .form-field {
      width: 100%;
    }

    .movimientos-section {
      margin: 24px 0;
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 16px;
    }

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
    }

    .section-header h3 {
      margin: 0;
      color: #333;
    }

    .movimiento-row {
      display: grid;
      grid-template-columns: 2fr 120px 150px 2fr 40px;
      gap: 12px;
      align-items: start;
      margin-bottom: 12px;
      padding: 12px;
      background-color: #f9f9f9;
      border-radius: 8px;
    }

    .cuenta-field, .tipo-field, .monto-field, .descripcion-field {
      margin-bottom: 0;
    }

    .totales-section {
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px solid #e0e0e0;
    }

    .totales-row {
      display: flex;
      justify-content: space-around;
      font-weight: 500;
      font-size: 16px;
    }

    .balanced {
      color: #4caf50;
    }

    .unbalanced {
      color: #f44336;
    }

    .form-actions {
      display: flex;
      gap: 16px;
      margin-top: 24px;
      justify-content: flex-end;
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

      .form-row {
        grid-template-columns: 1fr;
      }

      .movimiento-row {
        grid-template-columns: 1fr;
        gap: 8px;
      }

      .totales-row {
        flex-direction: column;
        gap: 8px;
        text-align: center;
      }

      .form-actions {
        flex-direction: column;
      }
    }
  `]
})
export class FormAsientoComponent implements OnInit {
  asientoForm: FormGroup;

  // Cuentas disponibles basadas en el caso "El Almacén El Planeador"
  cuentasDisponibles = [
    { id: 1, codigo: '1001', nombre: 'Efectivo en Caja' },
    { id: 2, codigo: '1002', nombre: 'Bancos' },
    { id: 3, codigo: '1101', nombre: 'Inventario de Mercaderías' },
    { id: 4, codigo: '1201', nombre: 'Mobiliario y Equipo' },
    { id: 5, codigo: '2001', nombre: 'Proveedores' },
    { id: 6, codigo: '2002', nombre: 'Letras de Cambio por Pagar' },
    { id: 7, codigo: '3001', nombre: 'Capital' },
    { id: 8, codigo: '4001', nombre: 'Ventas' },
    { id: 9, codigo: '5001', nombre: 'Alquiler' },
    { id: 10, codigo: '5002', nombre: 'Publicidad' },
    { id: 11, codigo: '5003', nombre: 'Sueldos' }
  ];

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {
    this.asientoForm = this.fb.group({
      fecha: [new Date(), Validators.required],
      tipo: ['OPERACION', Validators.required],
      descripcion: ['', [Validators.required, Validators.minLength(5)]],
      referencia: [''],
      movimientos: this.fb.array([])
    });

    // Agregar dos movimientos por defecto
    this.agregarMovimiento();
    this.agregarMovimiento();
  }

  ngOnInit(): void {
    // Suscribirse a cambios en los movimientos para calcular totales
    this.movimientos.valueChanges.subscribe(() => {
      this.calcularTotales();
    });
  }

  get movimientos(): FormArray {
    return this.asientoForm.get('movimientos') as FormArray;
  }

  get totalDebitos(): number {
    return this.movimientos.controls
      .filter(control => control.get('tipo')?.value === 'DEBITO')
      .reduce((sum, control) => sum + (control.get('monto')?.value || 0), 0);
  }

  get totalCreditos(): number {
    return this.movimientos.controls
      .filter(control => control.get('tipo')?.value === 'CREDITO')
      .reduce((sum, control) => sum + (control.get('monto')?.value || 0), 0);
  }

  get diferencia(): number {
    return this.totalDebitos - this.totalCreditos;
  }

  agregarMovimiento(): void {
    const movimiento = this.fb.group({
      cuentaId: ['', Validators.required],
      tipo: ['DEBITO', Validators.required],
      monto: [0, [Validators.required, Validators.min(0.01)]],
      descripcion: ['']
    });

    this.movimientos.push(movimiento);
  }

  eliminarMovimiento(index: number): void {
    if (this.movimientos.length > 2) {
      this.movimientos.removeAt(index);
    }
  }

  private calcularTotales(): void {
    // Los getters se encargan del cálculo automáticamente
  }

  guardar(): void {
    if (this.asientoForm.valid && this.diferencia === 0) {
      const datosFormulario = this.asientoForm.value;
      console.log('Datos del asiento a guardar:', datosFormulario);

      // Aquí implementaremos la lógica de guardado
      // Por ahora solo navegamos de vuelta
      this.router.navigate(['/asientos']);
    }
  }
}
