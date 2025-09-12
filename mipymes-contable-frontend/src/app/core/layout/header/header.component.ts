// src/app/core/layout/header/header.component.ts - Header responsivo
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
        <!-- Botón de usuario con texto en pantallas grandes -->
        <button mat-button [matMenuTriggerFor]="userMenu" class="user-button-desktop">
          <mat-icon>account_circle</mat-icon>
          <span class="user-text">Usuario</span>
        </button>

        <!-- Botón de usuario solo icono en pantallas pequeñas -->
        <button mat-icon-button [matMenuTriggerFor]="userMenu" class="user-button-mobile">
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
      min-height: 64px;
    }

    .menu-button {
      margin-right: 8px;
    }

    .app-title {
      font-size: 18px;
      font-weight: 500;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .spacer {
      flex: 1 1 auto;
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .user-button-desktop {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .user-button-mobile {
      display: none;
    }

    .user-text {
      font-size: 14px;
    }

    /* Responsivo para tablets */
    @media screen and (max-width: 1024px) {
      .app-title {
        font-size: 16px;
      }

      .user-text {
        display: none;
      }
    }

    /* Responsivo para móviles */
    @media screen and (max-width: 768px) {
      .header-toolbar {
        min-height: 56px;
        padding: 0 8px;
      }

      .menu-button {
        margin-right: 4px;
      }

      .app-title {
        font-size: 14px;
        max-width: 120px;
      }

      .user-button-desktop {
        display: none;
      }

      .user-button-mobile {
        display: flex;
      }
    }

    /* Responsivo para móviles muy pequeños */
    @media screen and (max-width: 480px) {
      .header-toolbar {
        padding: 0 4px;
      }

      .app-title {
        font-size: 12px;
        max-width: 100px;
      }

      .menu-button {
        width: 40px;
        height: 40px;
      }

      .user-button-mobile {
        width: 40px;
        height: 40px;
      }
    }
  `]
})
export class HeaderComponent {
  @Output() menuToggle = new EventEmitter<void>();

  appName = environment.appName;
}
