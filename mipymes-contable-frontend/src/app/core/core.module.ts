// src/app/core/core.module.ts - Módulo core corregido
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// NOTA: Los componentes de layout ahora van en app.module.ts
// Este módulo es para servicios y otros elementos core que no sean componentes de layout

@NgModule({
  declarations: [
    // NO declaramos componentes aquí para evitar duplicación
  ],
  imports: [
    CommonModule
  ],
  providers: [
    // Aquí van los servicios core si los hubiera
  ]
})
export class CoreModule { }
