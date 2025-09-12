// src/app/shared/pipes/currency.pipe.ts - Pipe para formato de moneda
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appCurrency'
})
export class CurrencyPipe implements PipeTransform {
  transform(value: number | string | null | undefined, showSymbol: boolean = true): string {
    if (value === null || value === undefined || value === '') {
      return showSymbol ? 'Q 0.00' : '0.00';
    }

    const numValue = typeof value === 'string' ? parseFloat(value) : value;

    if (isNaN(numValue)) {
      return showSymbol ? 'Q 0.00' : '0.00';
    }

    const formatted = numValue.toLocaleString('es-GT', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });

    return showSymbol ? `Q ${formatted}` : formatted;
  }
}
