// src/app/features/features.module.ts - Módulo features corregido
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// Importamos SharedModule en lugar de declarar componentes compartidos
import { SharedModule } from '../shared/shared.module';

// Componentes específicos de features
import { DashboardComponent } from './dashboard/dashboard.component';
import { ListaCuentasComponent } from './cuentas/lista-cuentas/lista-cuentas.component';
import { FormCuentaComponent } from './cuentas/form-cuenta/form-cuenta.component';
import { ListaAsientosComponent } from './asientos/lista-asientos/lista-asientos.component';
import { FormAsientoComponent } from './asientos/form-asiento/form-asiento.component';

@NgModule({
  declarations: [
    DashboardComponent,
    ListaCuentasComponent,
    FormCuentaComponent,
    ListaAsientosComponent,
    FormAsientoComponent
  ],
  imports: [
    CommonModule,
    SharedModule // Aquí importamos todos los componentes y módulos compartidos
  ]
})
export class FeaturesModule { }
