// src/app/features/inventario/inventario.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { Component } from '@angular/core';

@Component({
  selector: 'app-inventario',
  template: `
    <div class="page-container">
      <h1>Gestión de Inventario</h1>
      <p>Control de productos y movimientos de inventario.</p>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
    }
  `]
})
export class InventarioComponent { }

const routes: Routes = [
  {
    path: '',
    component: InventarioComponent
  }
];

@NgModule({
  declarations: [
    InventarioComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class InventarioModule { }
