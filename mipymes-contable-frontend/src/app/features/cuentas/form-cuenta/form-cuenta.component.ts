// src/app/features/cuentas/form-cuenta/form-cuenta.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CuentaService } from '../../../core/services/cuenta.service';
import { CuentaDTO, TipoCuenta, NaturalezaCuenta, getTipoDescripcion, getNaturalezaDescripcion } from '../../../core/models/cuenta.model';

@Component({
  selector: 'app-form-cuenta',
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>{{ esEdicion ? 'Editar' : 'Nueva' }} Cuenta</h1>
        <button mat-stroked-button routerLink="/cuentas">
          <mat-icon>arrow_back</mat-icon>
          Volver
        </button>
      </div>

      <!-- Loading al cargar datos -->
      <div *ngIf="cargandoDatos" class="loading-container">
        <mat-spinner diameter="50"></mat-spinner>
        <p>Cargando datos de la cuenta...</p>
      </div>

      <!-- Error al cargar -->
      <div *ngIf="errorCarga && !cargandoDatos" class="error-container">
        <mat-icon color="warn">error</mat-icon>
        <p>{{ errorCarga }}</p>
        <button mat-raised-button color="primary" routerLink="/cuentas">
          Volver a Cuentas
        </button>
      </div>

      <!-- Formulario -->
      <mat-card *ngIf="!cargandoDatos && !errorCarga">
        <mat-card-header>
          <mat-card-title>Información de la Cuenta</mat-card-title>
          <mat-card-subtitle *ngIf="esEdicion">
            Editando cuenta: {{ cuentaForm.get('codigo')?.value }}
          </mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="cuentaForm" (ngSubmit)="guardar()" class="form-container">

            <!-- Primera fila: Código y Nombre -->
            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Código de Cuenta</mat-label>
                <input matInput
                       formControlName="codigo"
                       placeholder="Ej: 1001"
                       maxlength="10"
                       [readonly]="esEdicion">
                <mat-hint *ngIf="!esEdicion">Código único de 4 dígitos</mat-hint>
                <mat-error *ngIf="cuentaForm.get('codigo')?.hasError('required')">
                  El código es obligatorio
                </mat-error>
                <mat-error *ngIf="cuentaForm.get('codigo')?.hasError('pattern')">
                  El código debe tener exactamente 4 dígitos
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Nombre de la Cuenta</mat-label>
                <input matInput
                       formControlName="nombre"
                       placeholder="Ej: Efectivo en Caja"
                       maxlength="100">
                <mat-error *ngIf="cuentaForm.get('nombre')?.hasError('required')">
                  El nombre es obligatorio
                </mat-error>
                <mat-error *ngIf="cuentaForm.get('nombre')?.hasError('minlength')">
                  El nombre debe tener al menos 3 caracteres
                </mat-error>
              </mat-form-field>
            </div>

            <!-- Segunda fila: Tipo y Naturaleza -->
            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Tipo de Cuenta</mat-label>
                <mat-select formControlName="tipo" (selectionChange)="onTipoChange($event.value)">
                  <mat-option *ngFor="let tipo of tiposCuenta" [value]="tipo.value">
                    {{ tipo.descripcion }}
                  </mat-option>
                </mat-select>
                <mat-hint>Selecciona el tipo según la ecuación contable</mat-hint>
                <mat-error *ngIf="cuentaForm.get('tipo')?.hasError('required')">
                  El tipo es obligatorio
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Naturaleza</mat-label>
                <mat-select formControlName="naturaleza">
                  <mat-option *ngFor="let naturaleza of naturalezasCuenta" [value]="naturaleza.value">
                    {{ naturaleza.descripcion }}
                  </mat-option>
                </mat-select>
                <mat-hint>{{ getNaturalezaHint() }}</mat-hint>
                <mat-error *ngIf="cuentaForm.get('naturaleza')?.hasError('required')">
                  La naturaleza es obligatoria
                </mat-error>
              </mat-form-field>
            </div>

            <!-- Tercera fila: Saldo y Estado -->
            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Saldo Inicial</mat-label>
                <input matInput
                       type="number"
                       formControlName="saldo"
                       placeholder="0.00"
                       step="0.01"
                       min="0">
                <span matPrefix>Q&nbsp;</span>
                <mat-hint>Saldo inicial de la cuenta</mat-hint>
                <mat-error *ngIf="cuentaForm.get('saldo')?.hasError('min')">
                  El saldo no puede ser negativo
                </mat-error>
              </mat-form-field>

              <div class="form-field checkbox-field">
                <mat-checkbox formControlName="activa">
                  Cuenta Activa
                </mat-checkbox>
                <mat-hint>Las cuentas inactivas no aparecen en transacciones</mat-hint>
              </div>
            </div>

            <!-- Descripción -->
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Descripción (Opcional)</mat-label>
              <textarea matInput
                        formControlName="descripcion"
                        rows="3"
                        maxlength="500"
                        placeholder="Descripción adicional de la cuenta"></textarea>
              <mat-hint align="end">
                {{ cuentaForm.get('descripcion')?.value?.length || 0 }}/500
              </mat-hint>
            </mat-form-field>

            <!-- Preview de la cuenta -->
            <mat-card class="preview-card" *ngIf="cuentaForm.valid">
              <mat-card-header>
                <mat-card-title>Vista Previa</mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <div class="preview-info">
                  <p><strong>{{ cuentaForm.get('codigo')?.value }} - {{ cuentaForm.get('nombre')?.value }}</strong></p>
                  <p>Tipo: {{ getTipoDescripcion(cuentaForm.get('tipo')?.value) }}</p>
                  <p>Naturaleza: {{ getNaturalezaDescripcion(cuentaForm.get('naturaleza')?.value) }}</p>
                  <p>Saldo: {{ cuentaForm.get('saldo')?.value | currency:'GTQ':'symbol':'1.2-2' }}</p>
                </div>
              </mat-card-content>
            </mat-card>

            <!-- Acciones del formulario -->
            <div class="form-actions">
              <button mat-raised-button
                      color="primary"
                      type="submit"
                      [disabled]="!cuentaForm.valid || guardando">
                <mat-spinner diameter="20" *ngIf="guardando"></mat-spinner>
                <mat-icon *ngIf="!guardando">save</mat-icon>
                {{ guardando ? 'Guardando...' : (esEdicion ? 'Actualizar' : 'Guardar') }}
              </button>
              <button mat-stroked-button
                      type="button"
                      routerLink="/cuentas"
                      [disabled]="guardando">
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
      max-width: 800px;
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

    .checkbox-field {
      display: flex;
      flex-direction: column;
      justify-content: center;
    }

    .checkbox-field mat-hint {
      font-size: 12px;
      color: rgba(0,0,0,0.6);
      margin-top: 4px;
    }

    .preview-card {
      margin: 24px 0;
      background-color: #f8f9fa;
    }

    .preview-info p {
      margin: 4px 0;
    }

    .form-actions {
      display: flex;
      gap: 16px;
      margin-top: 24px;
      justify-content: flex-end;
    }

    .loading-container, .error-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px;
      text-align: center;
    }

    .loading-container p, .error-container p {
      margin-top: 16px;
      color: #666;
    }

    .error-container mat-icon {
      font-size: 48px;
      width: 48px;
      height: 48px;
      margin-bottom: 16px;
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

      .form-actions {
        flex-direction: column;
      }
    }
  `]
})
export class FormCuentaComponent implements OnInit {
  cuentaForm: FormGroup;
  esEdicion = false;
  cuentaId: number | null = null;
  cargandoDatos = false;
  guardando = false;
  errorCarga: string | null = null;

  // Opciones para los selects
  tiposCuenta = [
    { value: TipoCuenta.ACTIVO, descripcion: 'Activo' },
    { value: TipoCuenta.PASIVO, descripcion: 'Pasivo' },
    { value: TipoCuenta.PATRIMONIO, descripcion: 'Patrimonio' },
    { value: TipoCuenta.INGRESO, descripcion: 'Ingreso' },
    { value: TipoCuenta.GASTO, descripcion: 'Gasto' }
  ];

  naturalezasCuenta = [
    { value: NaturalezaCuenta.DEUDORA, descripcion: 'Deudora' },
    { value: NaturalezaCuenta.ACREEDORA, descripcion: 'Acreedora' }
  ];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private cuentaService: CuentaService,
    private snackBar: MatSnackBar
  ) {
    this.cuentaForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      tipo: ['', Validators.required],
      naturaleza: ['', Validators.required],
      saldo: [0, [Validators.min(0)]],
      activa: [true],
      descripcion: ['', Validators.maxLength(500)]
    });
  }

  ngOnInit(): void {
    // Verificar si estamos editando
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.esEdicion = true;
        this.cuentaId = +params['id'];
        this.cargarCuenta();
      }
    });
  }

  /**
   * Cargar datos de la cuenta para edición
   */
  cargarCuenta(): void {
    if (!this.cuentaId) return;

    this.cargandoDatos = true;
    this.errorCarga = null;

    this.cuentaService.obtenerCuentaPorId(this.cuentaId).subscribe({
      next: (cuenta) => {
        console.log('Cuenta cargada:', cuenta);
        this.cuentaForm.patchValue({
          codigo: cuenta.codigo,
          nombre: cuenta.nombre,
          tipo: cuenta.tipo,
          naturaleza: cuenta.naturaleza,
          saldo: cuenta.saldo,
          activa: cuenta.activa,
          descripcion: cuenta.descripcion
        });
        this.cargandoDatos = false;
      },
      error: (error) => {
        console.error('Error al cargar cuenta:', error);
        this.errorCarga = 'No se pudo cargar la cuenta. Verifique que existe.';
        this.cargandoDatos = false;
      }
    });
  }

  /**
   * Auto-completar naturaleza cuando cambia el tipo
   */
  onTipoChange(tipo: TipoCuenta): void {
    if (tipo === TipoCuenta.ACTIVO || tipo === TipoCuenta.GASTO) {
      this.cuentaForm.patchValue({ naturaleza: NaturalezaCuenta.DEUDORA });
    } else if (tipo === TipoCuenta.PASIVO || tipo === TipoCuenta.PATRIMONIO || tipo === TipoCuenta.INGRESO) {
      this.cuentaForm.patchValue({ naturaleza: NaturalezaCuenta.ACREEDORA });
    }
  }

  /**
   * Obtener hint para naturaleza
   */
  getNaturalezaHint(): string {
    const naturaleza = this.cuentaForm.get('naturaleza')?.value;
    if (naturaleza === NaturalezaCuenta.DEUDORA) {
      return 'Aumenta con débitos (Activos y Gastos)';
    } else if (naturaleza === NaturalezaCuenta.ACREEDORA) {
      return 'Aumenta con créditos (Pasivos, Patrimonio e Ingresos)';
    }
    return 'Selecciona la naturaleza de la cuenta';
  }

  /**
   * Guardar cuenta (crear o actualizar)
   */
  guardar(): void {
    if (!this.cuentaForm.valid) {
      this.marcarCamposInvalidos();
      return;
    }

    this.guardando = true;
    const datosFormulario: CuentaDTO = this.cuentaForm.value;

    const operacion = this.esEdicion
      ? this.cuentaService.actualizarCuenta(this.cuentaId!, datosFormulario)
      : this.cuentaService.crearCuenta(datosFormulario);

    operacion.subscribe({
      next: (cuenta) => {
        console.log('Cuenta guardada:', cuenta);
        this.mostrarExito(
          this.esEdicion
            ? 'Cuenta actualizada exitosamente'
            : 'Cuenta creada exitosamente'
        );
        this.router.navigate(['/cuentas']);
      },
      error: (error) => {
        console.error('Error al guardar cuenta:', error);
        this.guardando = false;

        // Manejar errores específicos
        if (error?.status === 409) {
          this.mostrarError('Ya existe una cuenta con ese código');
        } else {
          this.mostrarError('Error al guardar la cuenta');
        }
      }
    });
  }

  /**
   * Marcar campos inválidos para mostrar errores
   */
  private marcarCamposInvalidos(): void {
    Object.keys(this.cuentaForm.controls).forEach(key => {
      const control = this.cuentaForm.get(key);
      if (control && control.invalid) {
        control.markAsTouched();
      }
    });
  }

  /**
   * Funciones helper importadas
   */
  getTipoDescripcion = getTipoDescripcion;
  getNaturalezaDescripcion = getNaturalezaDescripcion;

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
