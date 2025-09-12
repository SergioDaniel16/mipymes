// src/app/shared/components/loading/loading.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-loading',
  template: `
    <div class="loading-container">
      <mat-spinner diameter="50"></mat-spinner>
      <p class="loading-text">Cargando...</p>
    </div>
  `,
  styles: [`
    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 16px;
    }

    .loading-text {
      margin: 0;
      font-size: 16px;
      color: #4caf50;
      font-weight: 500;
    }
  `]
})
export class LoadingComponent {}
