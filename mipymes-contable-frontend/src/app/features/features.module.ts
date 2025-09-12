// src/app/features/features.module.ts - Módulo features corregido
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// Importamos SharedModule en lugar de declarar componentes compartidos
import { SharedModule } from '../shared/shared.module';

// NO importamos componentes específicos de features porque van en sus propios módulos

@NgModule({
  declarations: [
    // Los componentes ahora están en sus propios módulos
  ],
  imports: [
    CommonModule,
    SharedModule // Aquí importamos todos los componentes y módulos compartidos
  ]
})
export class FeaturesModule { }
