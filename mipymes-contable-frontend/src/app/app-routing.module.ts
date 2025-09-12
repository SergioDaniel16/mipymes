// src/app/app-routing.module.ts - Rutas activadas
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './core/layout/main-layout/main-layout.component';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      // ✅ ACTIVAMOS ESTAS RUTAS QUE YA TIENEN MÓDULOS CREADOS
      {
        path: 'cuentas',
        loadChildren: () => import('./features/cuentas/cuentas.module').then(m => m.CuentasModule)
      },
      {
        path: 'asientos',
        loadChildren: () => import('./features/asientos/asientos.module').then(m => m.AsientosModule)
      },
      {
        path: 'balance',
        loadChildren: () => import('./features/balance/balance.module').then(m => m.BalanceModule)
      },
      {
        path: 'estados-financieros',
        loadChildren: () => import('./features/estados-financieros/estados-financieros.module').then(m => m.EstadosFinancierosModule)
      },
      {
        path: 'inventario',
        loadChildren: () => import('./features/inventario/inventario.module').then(m => m.InventarioModule)
      },
      {
        path: 'bancos',
        loadChildren: () => import('./features/bancos/bancos.module').then(m => m.BancosModule)
      },
      {
        path: 'clientes-proveedores',
        loadChildren: () => import('./features/clientes-proveedores/clientes-proveedores.module').then(m => m.ClientesProveedoresModule)
      }
    ]
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
