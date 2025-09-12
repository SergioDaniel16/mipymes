// src/app/features/asientos/asientos.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { ListaAsientosComponent } from './lista-asientos/lista-asientos.component';
import { FormAsientoComponent } from './form-asiento/form-asiento.component';

const routes: Routes = [
  {
    path: '',
    component: ListaAsientosComponent
  },
  {
    path: 'nuevo',
    component: FormAsientoComponent
  },
  {
    path: 'editar/:id',
    component: FormAsientoComponent
  }
];

@NgModule({
  declarations: [
    ListaAsientosComponent,
    FormAsientoComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class AsientosModule { }
