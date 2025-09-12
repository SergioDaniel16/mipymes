import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
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
    CommonModule
  ]
})
export class FeaturesModule { }
