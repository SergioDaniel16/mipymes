// src/app/features/cuentas/form-cuenta/form-cuenta.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

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

      <mat-card>
        <mat-card-header>
          <mat-card-title>Información de la Cuenta</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <form [formGroup]="cuentaForm" (ngSubmit)="guardar()" class="form-container">
            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Código de Cuenta</mat-label>
                <input matInput formControlName="codigo" placeholder="Ej: 1001" maxlength="10">
                <mat-error *ngIf="cuentaForm.get('codigo')?.hasError('required')">
                  El código es obligatorio
                </mat-error>
                <mat-error *ngIf="cuentaForm.get('codigo')?.hasError('pattern')">
                  Solo se permiten números
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Nombre de la Cuenta</mat-label>
                <input matInput formControlName="nombre" placeholder="Ej: Efectivo en Caja">
                <mat-error *ngIf="cuentaForm.get('nombre')?.hasError('required')">
                  El nombre es obligatorio
                </mat-error>
              </mat-form-field>
            </div>

            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Tipo de Cuenta</mat-label>
                <mat-select formControlName="tipo">
                  <mat-option value="ACTIVO">Activo</mat-option>
                  <mat-option value="PASIVO">Pasivo</mat-option>
                  <mat-option value="PATRIMONIO">Patrimonio</mat-option>
                  <mat-option value="INGRESO">Ingreso</mat-option>
                  <mat-option value="GASTO">Gasto</mat-option>
                </mat-select>
                <mat-error *ngIf="cuentaForm.get('tipo')?.hasError('required')">
                  El tipo es obligatorio
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Naturaleza</mat-label>
                <mat-select formControlName="naturaleza">
                  <mat-option value="DEUDORA">Deudora</mat-option>
                  <mat-option value="ACREEDORA">Acreedora</mat-option>
                </mat-select>
                <mat-error *ngIf="cuentaForm.get('naturaleza')?.hasError('required')">
                  La naturaleza es obligatoria
                </mat-error>
              </mat-form-field>
            </div>

            <div class="form-row">
              <mat-form-field appearance="outline" class="form-field">
                <mat-label>Saldo Inicial</mat-label>
                <input matInput type="number" formControlName="saldo" placeholder="0.00" step="0.01">
              </mat-form-field>

              <div class="form-field">
                <mat-checkbox formControlName="activa">Cuenta Activa</mat-checkbox>
              </div>
            </div>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Descripción (Opcional)</mat-label>
              <textarea matInput formControlName="descripcion" rows="3" placeholder="Descripción de la cuenta"></textarea>
            </mat-form-field>

            <div class="form-actions">
              <button mat-raised-button color="primary" type="submit" [disabled]="!cuentaForm.valid">
                <mat-icon>save</mat-icon>
                {{ esEdicion ? 'Actualizar' : 'Guardar' }}
              </button>
              <button mat-stroked-button type="button" routerLink="/cuentas">
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

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.cuentaForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      tipo: ['', Validators.required],
      naturaleza: ['', Validators.required],
      saldo: [0, [Validators.min(0)]],
      activa: [true],
      descripcion: ['']
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

    // Auto-completar naturaleza basada en el tipo
    this.cuentaForm.get('tipo')?.valueChanges.subscribe(tipo => {
      if (tipo === 'ACTIVO' || tipo === 'GASTO') {
        this.cuentaForm.patchValue({ naturaleza: 'DEUDORA' });
      } else if (tipo === 'PASIVO' || tipo === 'PATRIMONIO' || tipo === 'INGRESO') {
        this.cuentaForm.patchValue({ naturaleza: 'ACREEDORA' });
      }
    });
  }

  cargarCuenta(): void {
    // Aquí cargaríamos los datos de la cuenta desde el servicio
    // Por ahora simulamos con datos de ejemplo
    const cuentaEjemplo = {
      codigo: '1001',
      nombre: 'Efectivo en Caja',
      tipo: 'ACTIVO',
      naturaleza: 'DEUDORA',
      saldo: 38100,
      activa: true,
      descripcion: 'Dinero en efectivo disponible en caja'
    };

    this.cuentaForm.patchValue(cuentaEjemplo);
  }

  guardar(): void {
    if (this.cuentaForm.valid) {
      const datosFormulario = this.cuentaForm.value;
      console.log('Datos a guardar:', datosFormulario);

      // Aquí implementaremos la lógica de guardado
      // Por ahora solo navegamos de vuelta
      this.router.navigate(['/cuentas']);
    }
  }
}
