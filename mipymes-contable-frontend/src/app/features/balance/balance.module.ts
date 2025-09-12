// src/app/features/balance/balance.module.ts - Módulo corregido
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { BalanceComponent } from './balance.component';

const routes: Routes = [
  {
    path: '',
    component: BalanceComponent
  }
];

@NgModule({
  declarations: [
    BalanceComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class BalanceModule { }
