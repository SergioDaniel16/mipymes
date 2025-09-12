// src/app/shared/components/error-message/error-message.component.ts
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-error-message',
  template: `
    <div *ngIf="message" class="error-container" [ngClass]="{'full-screen': fullScreen}">
      <mat-icon class="error-icon">error_outline</mat-icon>
      <h3 class="error-title">{{ title || 'Error' }}</h3>
      <p class="error-message">{{ message }}</p>
      <button *ngIf="showRetry" mat-raised-button color="primary" (click)="onRetry()">
        Reintentar
      </button>
    </div>
  `,
  styles: [`
    .error-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 32px;
      text-align: center;
    }

    .full-screen {
      min-height: 400px;
    }

    .error-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      color: #f44336;
      margin-bottom: 16px;
    }

    .error-title {
      margin: 0 0 16px 0;
      color: #f44336;
      font-size: 24px;
    }

    .error-message {
      margin: 0 0 24px 0;
      font-size: 16px;
      color: rgba(0,0,0,0.7);
      max-width: 400px;
      line-height: 1.5;
    }
  `]
})
export class ErrorMessageComponent {
  @Input() message: string = '';
  @Input() title: string = '';
  @Input() showRetry: boolean = false;
  @Input() fullScreen: boolean = false;

  onRetry(): void {
    // Emit retry event or handle retry logic
    window.location.reload();
  }
}
