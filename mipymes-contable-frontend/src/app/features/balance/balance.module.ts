// src/app/features/balance/balance.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

// Crear el componente básico
import { Component } from '@angular/core';

@Component({
  selector: 'app-balance',
  template: `
    <div class="page-container">
      <h1>Balance de Comprobación</h1>
      <p>Próximamente implementaremos el balance de comprobación aquí.</p>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
    }
  `]
})
export class BalanceComponent { }

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
