// src/app/features/cuentas/cuentas.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { ListaCuentasComponent } from './lista-cuentas/lista-cuentas.component';
import { FormCuentaComponent } from './form-cuenta/form-cuenta.component';

const routes: Routes = [
  {
    path: '',
    component: ListaCuentasComponent
  },
  {
    path: 'nueva',
    component: FormCuentaComponent
  },
  {
    path: 'editar/:id',
    component: FormCuentaComponent
  }
];

@NgModule({
  declarations: [
    ListaCuentasComponent,
    FormCuentaComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class CuentasModule { }
