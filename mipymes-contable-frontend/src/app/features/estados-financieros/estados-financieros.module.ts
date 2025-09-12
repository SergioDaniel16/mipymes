// src/app/features/estados-financieros/estados-financieros.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { EstadosFinancierosComponent } from './estados-financieros.component';

const routes: Routes = [
  {
    path: '',
    component: EstadosFinancierosComponent
  }
];

@NgModule({
  declarations: [
    EstadosFinancierosComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class EstadosFinancierosModule { }
