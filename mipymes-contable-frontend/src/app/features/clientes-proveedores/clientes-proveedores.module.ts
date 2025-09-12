// src/app/features/clientes-proveedores/clientes-proveedores.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { Component } from '@angular/core';

@Component({
  selector: 'app-clientes-proveedores',
  template: `
    <div class="page-container">
      <h1>Clientes y Proveedores</h1>
      <p>Gestión de clientes, proveedores y cuentas por cobrar/pagar.</p>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
    }
  `]
})
export class ClientesProveedoresComponent { }

const routes: Routes = [
  {
    path: '',
    component: ClientesProveedoresComponent
  }
];

@NgModule({
  declarations: [
    ClientesProveedoresComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class ClientesProveedoresModule { }
