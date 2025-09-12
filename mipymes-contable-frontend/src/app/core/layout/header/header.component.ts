// src/app/core/layout/header/header.component.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-header',
  template: `
    <mat-toolbar color="primary" class="header-toolbar">
      <button
        type="button"
        aria-label="Toggle sidenav"
        mat-icon-button
        (click)="menuToggle.emit()"
        class="menu-button">
        <mat-icon aria-label="Side nav toggle icon">menu</mat-icon>
      </button>

      <span class="app-title">{{ appName }}</span>

      <span class="spacer"></span>

      <div class="header-actions">
        <button mat-icon-button [matMenuTriggerFor]="userMenu">
          <mat-icon>account_circle</mat-icon>
        </button>
        <mat-menu #userMenu="matMenu">
          <button mat-menu-item>
            <mat-icon>person</mat-icon>
            <span>Perfil</span>
          </button>
          <button mat-menu-item>
            <mat-icon>settings</mat-icon>
            <span>Configuración</span>
          </button>
          <mat-divider></mat-divider>
          <button mat-menu-item>
            <mat-icon>exit_to_app</mat-icon>
            <span>Cerrar Sesión</span>
          </button>
        </mat-menu>
      </div>
    </mat-toolbar>
  `,
  styles: [`
    .header-toolbar {
      position: sticky;
      top: 0;
      z-index: 1000;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .menu-button {
      margin-right: 16px;
    }

    .app-title {
      font-size: 20px;
      font-weight: 500;
    }

    .spacer {
      flex: 1 1 auto;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    @media (max-width: 768px) {
      .app-title {
        font-size: 16px;
      }

      .menu-button {
        margin-right: 8px;
      }
    }
  `]
})
export class HeaderComponent {
  @Output() menuToggle = new EventEmitter<void>();

  appName = environment.appName;
}
