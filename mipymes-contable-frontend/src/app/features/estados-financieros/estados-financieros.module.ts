// src/app/features/estados-financieros/estados-financieros.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { Component } from '@angular/core';

@Component({
  selector: 'app-estados-financieros',
  template: `
    <div class="page-container">
      <h1>Estados Financieros</h1>
      <p>Aquí se generarán el Balance General y Estado de Resultados.</p>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
    }
  `]
})
export class EstadosFinancierosComponent { }

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
