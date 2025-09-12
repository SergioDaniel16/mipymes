// src/app/features/bancos/bancos.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { Component } from '@angular/core';

@Component({
  selector: 'app-bancos',
  template: `
    <div class="page-container">
      <h1>Gestión Bancaria</h1>
      <p>Cuentas bancarias, movimientos y conciliación.</p>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
    }
  `]
})
export class BancosComponent { }

const routes: Routes = [
  {
    path: '',
    component: BancosComponent
  }
];

@NgModule({
  declarations: [
    BancosComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class BancosModule { }
